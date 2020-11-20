package com.example.decide_o;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth nAuth;
    private Toolbar nToolbar;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private MenuItem item;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nAuth = FirebaseAuth.getInstance();



    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser crntuser = nAuth.getCurrentUser();
        if (crntuser == null) {
            Intent startintent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(startintent);
            finish();


        }
    }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            return true;

        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            super.onOptionsItemSelected(item);
            if (item.getItemId() == R.id.menu_lo) {
                FirebaseAuth.getInstance().signOut();
                Intent startintent = new Intent(MainActivity.this, StartActivity.class);
                startActivity(startintent);

            }
            if(item.getItemId()==R.id.search){
                Intent searchintent = new Intent(MainActivity.this, search.class);
                startActivity(searchintent);

            }
            if(item.getItemId()== R.id.notification){
                Intent notifintent = new Intent(MainActivity.this, search.class);
                startActivity(notifintent);

            }
            if(item.getItemId()==R.id.userlist){

            }
            return true;
        }

}
