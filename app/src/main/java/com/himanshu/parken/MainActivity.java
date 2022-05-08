package com.himanshu.parken;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Button buttonSignIn = findViewById(R.id.button_signin);
        buttonSignIn.setOnClickListener(view -> {
            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentLogin);

        });

        Button buttonSignUp = findViewById(R.id.button_signup);
        buttonSignUp.setOnClickListener(view -> {
            Intent intentLogin = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intentLogin);

        });

    }
}