package com.example.decide_o;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class AllUsers extends AppCompatActivity {
    private RecyclerView nuserlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        nuserlist=(RecyclerView) findViewById(R.id.userlist);

    }
}