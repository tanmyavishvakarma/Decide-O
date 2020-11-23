package com.example.decide_o;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

public class AllUsers extends AppCompatActivity {
    private RecyclerView nuserlist;
    private DatabaseReference nUserDatabase;
    private LinearLayoutManager nlayoutmanager;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        nUserDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        nlayoutmanager=new LinearLayoutManager(this);
        nuserlist = (RecyclerView) findViewById(R.id.userlist);
        nuserlist.setHasFixedSize(true);
        nuserlist.setLayoutManager(nlayoutmanager);
    }

    @Override
    protected void onStart() {
        super.onStart();
       FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.single_users,
                UsersViewHolder.class,
                nUserDatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, final Users users, int position) {
                usersViewHolder.setName(users.getName());

                final String user_id=getRef(position).getKey();

                usersViewHolder.nView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileintent=new Intent(AllUsers.this,ProfilePage.class);
                        profileintent.putExtra("user_id",user_id);
                        startActivity(profileintent);

                    }
                });
            }
        };
        nuserlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View nView;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            nView=itemView;

        }

        public void setName(String name) {
            TextView nameview=(TextView)nView.findViewById(R.id.single_username);
            nameview.setText(name);
        }
    }
}