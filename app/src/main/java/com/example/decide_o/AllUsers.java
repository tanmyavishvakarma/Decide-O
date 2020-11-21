package com.example.decide_o;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllUsers extends AppCompatActivity {
    private RecyclerView nuserlist;
    private DatabaseReference  nUserDatabase;
    private FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        nUserDatabase= FirebaseDatabase.getInstance().getReference();
        nuserlist=findViewById(R.id.userlist);
        nuserlist.setLayoutManager(new LinearLayoutManager(this));
        nuserlist.setHasFixedSize(true);
        nuserlist.setLayoutManager(new LinearLayoutManager(this));

        Query query=nUserDatabase.child("Users");

        FirebaseRecyclerOptions<Users> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(query, new SnapshotParser<Users>() {
                    @NonNull
                    @Override
                    public Users parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Users(snapshot.child("name").getValue().toString());
                    }
                })
                .build();

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users,UsersViewHolder>(firebaseRecyclerOptions){

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_users,parent,false);
                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, final int position, @NonNull Users model) {
                holder.setName(model.getName());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(AllUsers.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public TextView username;
        View nView;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            root=itemView.findViewById(R.id.list_root);
            username=itemView.findViewById(R.id.single_username);


        }
        public void setName(String name){
           username.setText(name);
        }
    }
}