package com.himanshu.parken;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.himanshu.parken.user.UserProfileActivity;
import com.himanshu.parken.user.authentication.LoginActivity;
import com.himanshu.parken.user.authentication.SignupActivity;

import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Button buttonSignIn = findViewById(R.id.button_welcome_sign_in);
        buttonSignIn.setOnClickListener(view -> {
            Intent intentSignIn = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intentSignIn);

        });

        Button buttonSignUp = findViewById(R.id.button_welcome_sign_up);
        buttonSignUp.setOnClickListener(view -> {
            Intent intentSignUp = new Intent(WelcomeActivity.this, SignupActivity.class);
            startActivity(intentSignUp);

        });

    }
}