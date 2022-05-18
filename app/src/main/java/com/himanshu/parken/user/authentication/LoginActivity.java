package com.himanshu.parken.user.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.himanshu.parken.R;
import com.himanshu.parken.user.UserProfileActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();

        etEmail = findViewById(R.id.textInputEditText_signin_email);
        etPassword = findViewById(R.id.textInputEditText_signin_password);

        TextView tvForgotPassword = findViewById(R.id.textView_signin_forgot_password);
        TextView tvSignUp = findViewById(R.id.textView_signin_signup);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar_signin);

        Button btSignIn = findViewById(R.id.button_signin_signin);
        btSignIn.setOnClickListener(view -> signInUser());

        tvForgotPassword.setOnClickListener(view -> forgotPassword());
        tvSignUp.setOnClickListener(view -> signUp());

    }

    private void signInUser() {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

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

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password required!");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.INVISIBLE);
                FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                if (fbUser == null) {
                    Toast.makeText(this, "could not find user data!", Toast.LENGTH_LONG).show();
                }
                if (Objects.requireNonNull(fbUser).isEmailVerified()) {
                    Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Email not verified");
                    builder.setMessage("Your Email is not verified! \nVerify it before signing in.")
                            .setCancelable(false)
                            .setNeutralButton(android.R.string.ok, (dialog, id) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();

                    fbUser.sendEmailVerification();
                    Toast.makeText(this, "An email verification is sent to you.", Toast.LENGTH_SHORT).show();
                }
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthWeakPasswordException | FirebaseAuthUserCollisionException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    Toast.makeText(LoginActivity.this, "incorrect email or password!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void forgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void signUp() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}