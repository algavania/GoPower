package com.example.gopower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    TextView tv_profile;
    Button btn_logout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Init variables
        tv_profile = findViewById(R.id.tv_profile);
        btn_logout = findViewById(R.id.btn_logout);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Logout button
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                checkUserStatus();
            }
        });

    }

    private void checkUserStatus(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            tv_profile.setText(user.getEmail());
        } else {
            // No user is signed in
            startActivity(new Intent(Profile.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        checkUserStatus();
    }
}
