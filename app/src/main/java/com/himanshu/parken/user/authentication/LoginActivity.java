package com.himanshu.parken.user.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.himanshu.parken.R;
import com.himanshu.parken.core.UserProfileActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextView tvForgotPassword, tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();

        etEmail = findViewById(R.id.editText_signin_email);
        etPassword = findViewById(R.id.editText_signin_password);

        tvForgotPassword = findViewById(R.id.textView_signin_forgot_password);
        tvSignUp = findViewById(R.id.textView_signin_signup);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar_signin);

        Button btSignIn = findViewById(R.id.button_signin_signin);
        btSignIn.setOnClickListener(view -> signInUser());

        tvForgotPassword.setOnClickListener(view -> forgotPassword());
        tvSignUp.setOnClickListener(view -> signUp());

    }

    private void signInUser(){

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email required!");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Enter a valid Email!");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password required!");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(LoginActivity.this, "incorrect email or password!", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.INVISIBLE);
        });


    }

    private void forgotPassword(){
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void signUp(){
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}