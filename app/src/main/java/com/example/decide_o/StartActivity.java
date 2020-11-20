package com.example.decide_o;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
    private Button registerbtn;
    private Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loginbtn=(Button) findViewById(R.id.start_login_btn);
        registerbtn=(Button) findViewById(R.id.start_reg_btn);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regintent=new Intent(StartActivity.this,registeration.class);
                startActivity(regintent);

            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logintent=new Intent(StartActivity.this,login.class);
                startActivity(logintent);
                finish();
            }
        });
    }


}