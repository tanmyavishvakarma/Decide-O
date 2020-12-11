package com.example.decide_o;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.util.HashMap;

public class registeration extends AppCompatActivity {
    private TextInputLayout nemail;
    private TextInputLayout nname;
    private EditText npass;
    private Button regbtn;
    private  Toolbar nToolbar;
    private FirebaseAuth nAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private DatabaseReference nDatabase;
    private ProgressDialog nRegProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        nRegProgress=new ProgressDialog(this);
        nAuth = FirebaseAuth.getInstance();
        nemail = (TextInputLayout) findViewById(R.id.email_input);
        nname = (TextInputLayout) findViewById(R.id.nameinput);
        npass = (EditText) findViewById(R.id.pass);
        regbtn = (Button) findViewById(R.id.registerbtn);


        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = nname.getEditText().getText().toString();
                String femail = nemail.getEditText().getText().toString();
                String fpass = npass.getText().toString();

                if(!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(femail) && !TextUtils.isEmpty(fpass)){
                    nRegProgress.setTitle("Registering User");
                    nRegProgress.setMessage("Please wait while we create your account");
                    nRegProgress.show();
                    register_user(femail, fpass, fname);
                }


            }
        });
    }

    private void register_user(String femail, String fpass, final String fname) {

        nAuth.createUserWithEmailAndPassword(femail, fpass).addOnCompleteListener(registeration.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
                    String uid=currentuser.getUid();
                    nDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    String device_token= FirebaseInstanceId.getInstance().getToken();
                    HashMap<String,String> userMap=new HashMap<>();
                    userMap.put("name",fname);
                    userMap.put("device_tokens",device_token);

                    nDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                nRegProgress.dismiss();
                                Toast.makeText(registeration.this, "Sign Up Complete", Toast.LENGTH_SHORT).show();

                                Intent mainintent = new Intent(registeration.this, MainActivity.class);
                                mainintent.addCategory(String.valueOf(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                startActivity(mainintent);
                                finish();
                            }
                        }
                    });


                } else {
                    nRegProgress.hide();
                    Toast.makeText(registeration.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

    /*
    @Override
    protected  void onStart(){
        super.onStart();
        nAuth.addAuthStateListener(firebaseAuthStateListener);
    }
    @Override
    protected void onStop(){
        super.onStop();
        nAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
*/





