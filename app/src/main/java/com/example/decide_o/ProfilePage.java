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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class ProfilePage extends AppCompatActivity {
    private TextView ndisplay;
    private Button nrqstbtn;
    private Button ndecline;
    private DatabaseReference nUserDatabase;
    private DatabaseReference nRootRef;
    private ProgressDialog  nProgressDialog;
    private DatabaseReference nnotificationdatabase;
    private DatabaseReference nfriendrqsdatabase;
    private FirebaseUser ncurrent_user;
    private String ncurrent_state;
    private DatabaseReference nfrienddatabase;
    private FirebaseAuth nAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        final String user_id=getIntent().getStringExtra("user_id");
        nRootRef=FirebaseDatabase.getInstance().getReference();


        nnotificationdatabase=FirebaseDatabase.getInstance().getReference().child("notification");
        nUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        nfriendrqsdatabase=FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        nfrienddatabase=FirebaseDatabase.getInstance().getReference().child("Friends");
        ncurrent_user= FirebaseAuth.getInstance().getCurrentUser();
        ndisplay=(TextView) findViewById(R.id.display_name);
        nrqstbtn=(Button)findViewById(R.id.request_btn);
        ndecline=(Button) findViewById(R.id.decline_btn);

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
                if(ncurrent_user.getUid().equals(user_id)){
                    nrqstbtn.setEnabled(false);
                    nrqstbtn.setVisibility(View.INVISIBLE);
                   ndecline.setEnabled(false);
                    ndecline.setVisibility(View.INVISIBLE);
                }



                //accept rqst
                nfriendrqsdatabase.child(ncurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(user_id)) {
                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                            if (req_type.equals("received")) {
                                ncurrent_state = "req_received";
                                nrqstbtn.setText("Accept Friend Request");
                                ndecline.setEnabled(true);
                                ndecline.setVisibility(View.VISIBLE);
                            } else if (req_type.equals("sent")) {
                                ncurrent_state = "req_sent";
                                nrqstbtn.setText("Cancel Friend Request");
                                ndecline.setEnabled(false);
                                ndecline.setVisibility(View.INVISIBLE);
                            }
                            nProgressDialog.dismiss();
                        }else{
                            nfrienddatabase.child(ncurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(user_id)){
                                        ncurrent_state="friends";
                                         nrqstbtn.setText("Unfriend");
                                        ndecline.setEnabled(false);
                                        ndecline.setVisibility(View.INVISIBLE);
                                    }
                                    nProgressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    nProgressDialog.dismiss();
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nrqstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nrqstbtn.setEnabled(false);
                //not friends
                if(ncurrent_state.equals("not_friends")){
                    nfriendrqsdatabase.child(ncurrent_user.getUid()).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                nfriendrqsdatabase.child(user_id).child(ncurrent_user.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ndecline.setEnabled(false);
                                        ndecline.setVisibility(View.INVISIBLE);
                                        nrqstbtn.setEnabled(true);
                                        HashMap<String,String> notificationData=new HashMap<>();
                                        notificationData.put("from",ncurrent_user.getUid());
                                        notificationData.put("type","request");

                                        nnotificationdatabase.child(user_id).push().setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                ncurrent_state="req_sent";
                                                nrqstbtn.setText("Cancel Friend Request");
                                                ndecline.setEnabled(false);
                                                ndecline.setVisibility(View.INVISIBLE);
                                                Toast.makeText(ProfilePage.this,"Request Sent",Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }
                                });
                            }else{
                                Toast.makeText(ProfilePage.this,"Failed Sending Request",Toast.LENGTH_SHORT).show();
                            }
                            nrqstbtn.setEnabled(true);
                        }
                    });
                }
                //cancel rqst
                if(ncurrent_state.equals("req_sent")){
                    nfriendrqsdatabase.child(ncurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            nfriendrqsdatabase.child(user_id).child(ncurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    nrqstbtn.setEnabled(true);
                                    ncurrent_state="not_friends";
                                    ndecline.setEnabled(false);
                                    ndecline.setVisibility(View.INVISIBLE);
                                    nrqstbtn.setText("Send Friend Request");
                                }
                            });
                        }
                    });
                }
//req recoieved
                if(ncurrent_state.equals("req_received")){
                    final String currentDate= DateFormat.getDateTimeInstance().format(new Date());
                    nfrienddatabase.child(ncurrent_user.getUid()).child(user_id).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            nfrienddatabase.child(user_id).child(ncurrent_user.getUid()).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    nfriendrqsdatabase.child(ncurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            nfriendrqsdatabase.child(user_id).child(ncurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    nrqstbtn.setEnabled(true);
                                                    ncurrent_state="friends";
                                                    ndecline.setEnabled(false);
                                                    ndecline.setVisibility(View.INVISIBLE);
                                                    nrqstbtn.setText("UnFriend");
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }

        });

    }
}