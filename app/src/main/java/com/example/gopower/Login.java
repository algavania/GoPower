package com.example.gopower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {


    EditText txt_email_login, txt_password_login;
    TextView tv_account_login;
    Button btn_continue_login;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init variables
        txt_email_login = findViewById(R.id.txt_email_login);
        txt_password_login = findViewById(R.id.txt_password_login);
        tv_account_login = findViewById(R.id.tv_account_login);
        btn_continue_login = findViewById(R.id.btn_continue_login);

        //Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("We're logging you in...");


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Login button
        btn_continue_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email_login.getText().toString().trim();
                String password = txt_password_login.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isEmpty()){
                    txt_email_login.setError("Invalid Email");
                    txt_password_login.setError("Invalid Password");
                    txt_email_login.setFocusable(true);
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && !password.isEmpty()){
                    txt_email_login.setError("Invalid Email");
                    txt_email_login.setFocusable(true);
                }else if(Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isEmpty()){
                    txt_password_login.setError("Invalid Password");
                    txt_password_login.setFocusable(true);
                }else{
                    loginUser(email, password);
                }
            }
        });

        //Don't have account button
        tv_account_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

    }

    private void loginUser(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(Login.this, Profile.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
