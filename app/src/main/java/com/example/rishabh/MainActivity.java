package com.example.rishabh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseUser user = mAuth.getCurrentUser();
        //Toast.makeText(this, "No user signed in",
        //        Toast.LENGTH_SHORT).show();
        FirebaseUser user = mAuth.getCurrentUser();
        //mAuth.signOut();
        if(user==null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            //mAuth.signOut();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            //mAuth.signOut();
            finish();
        }
    }


}
