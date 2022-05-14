package com.himanshu.parken.user.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.himanshu.parken.R;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etEmail;
    private Button btResetPassword;
    FirebaseAuth fbAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Objects.requireNonNull(getSupportActionBar()).hide();

        etEmail = findViewById(R.id.editText_forgot_password_email);
        btResetPassword = findViewById(R.id.button_forgot_password_reset);
        progressBar = findViewById(R.id.progressBar_forgot_password);

        fbAuth = FirebaseAuth.getInstance();

        btResetPassword.setOnClickListener(view -> resetPassword());

    }

    private void resetPassword(){

        String email = etEmail.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email required!");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid Email!");
            etEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        fbAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.INVISIBLE);
            if(task.isSuccessful()){
                Toast.makeText(ForgotPasswordActivity.this, "check your email for password reset!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(ForgotPasswordActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}