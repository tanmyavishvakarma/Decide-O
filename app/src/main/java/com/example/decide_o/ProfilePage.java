package com.example.decide_o;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity {
    private TextView ndisplay;
    private Button nrqstbtn;
    private DatabaseReference nUserDatabase;
    private DatabaseReference nRootRef;
    private ProgressDialog  nProgressDialog;
    private DatabaseReference nfriendrqsdatabase;
    private FirebaseUser ncurrent_user;
    private String ncurrent_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        final String user_id=getIntent().getStringExtra("user_id");
        nRootRef=FirebaseDatabase.getInstance().getReference();
        nUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        nfriendrqsdatabase=FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        ncurrent_user= FirebaseAuth.getInstance().getCurrentUser();
        ndisplay=(TextView) findViewById(R.id.display_name);
        nrqstbtn=(Button)findViewById(R.id.request_btn);

        ncurrent_state="not_friends";
        nProgressDialog =new ProgressDialog(this);
        nProgressDialog.setTitle("Loading User Data");
        nProgressDialog.setMessage("Please wait while we load the user data");
        nProgressDialog.setCanceledOnTouchOutside(false);
        nProgressDialog.show();

        nUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String display_name=dataSnapshot.child("name").getValue().toString();
                ndisplay.setText(display_name);
                nProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nrqstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ncurrent_state.equals("not_friends")){
                    nfriendrqsdatabase.child(ncurrent_user.getUid()).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                nfriendrqsdatabase.child(user_id).child(ncurrent_user.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProfilePage.this,"Request Sent",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(ProfilePage.this,"Failed Sending Request",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}