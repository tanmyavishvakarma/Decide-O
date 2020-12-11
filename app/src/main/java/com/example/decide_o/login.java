package com.example.decide_o;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

public class login extends AppCompatActivity {
    private TextInputLayout nemail;
    private EditText npass;
    private Button nlogbtn;
    private ProgressDialog nLogProgress;
    private FirebaseAuth nAuth;
    private DatabaseReference nUserDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nAuth= FirebaseAuth.getInstance();
        nemail=(TextInputLayout) findViewById(R.id.email);
        npass = (EditText) findViewById(R.id.pass);
        nlogbtn=(Button) findViewById(R.id.loginbtn);
        nLogProgress =new ProgressDialog(this);
        nUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        nlogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String femail = nemail.getEditText().getText().toString();
                String fpass = npass.getText().toString();
                if(!TextUtils.isEmpty(femail) && !TextUtils.isEmpty(fpass)){
                    nLogProgress.setTitle("Logging User");
                    nLogProgress.setMessage("Please wait while we check your credentials");
                    nLogProgress.setCanceledOnTouchOutside(false);
                    nLogProgress.show();
                    loginUser(femail,fpass);
                }

            }
        });
    }
    private void loginUser(String femail,String fpass){

        nAuth.signInWithEmailAndPassword(femail,fpass).addOnCompleteListener(login.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    nLogProgress.dismiss();
                    String current_user_id=nAuth.getCurrentUser().getUid();
                    String deviceToken= FirebaseInstanceId.getInstance().getToken();
                    nUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(login.this, "Login Complete", Toast.LENGTH_SHORT).show();
                            Intent mainIntent=new Intent(login.this,MainActivity.class);
                            mainIntent.addCategory(String.valueOf(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            startActivity(mainIntent);
                            finish();
                        }
                    });


                }else{
                    nLogProgress.hide();
                    Toast.makeText(login.this, "Login error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}