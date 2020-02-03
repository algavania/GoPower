package com.example.gopower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    EditText txt_email_register, txt_password_register;
    TextView tv_account_register;
    Button btn_continue_register;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Init variables
        txt_email_register = findViewById(R.id.txt_email_register);
        txt_password_register = findViewById(R.id.txt_password_register);
        tv_account_register = findViewById(R.id.tv_account_register);
        btn_continue_register = findViewById(R.id.btn_continue_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("We're registering you...");

        //Register button
        btn_continue_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email_register.getText().toString().trim();
                String password = txt_password_register.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    txt_email_register.setError("Invalid Email");
                    txt_email_register.setFocusable(true);
                }else if(password.length() < 6){
                    txt_password_register.setError("Password length must be 6 characters or more");
                    txt_password_register.setFocusable(true);
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isEmpty()){
                    txt_email_register.setError("Invalid Email");
                    txt_password_register.setError("Invalid Password");
                    txt_email_register.setFocusable(true);
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && !password.isEmpty()){
                    txt_email_register.setError("Invalid Email");
                    txt_email_register.setFocusable(true);
                }else if(Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isEmpty()){
                    txt_password_register.setError("Invalid Password");
                    txt_password_register.setFocusable(true);
                }else{
                    registerUser(email, password);
                }
            }
        });

        //Don't have account button
        tv_account_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

    }

    private void registerUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dismiss dialog and start register activity
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "You've already registered!\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, Profile.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
