package com.himanshu.parken;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.himanshu.parken.user.UserProfileActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemClock.sleep(300);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intentMain = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();
        } else {
            Intent intentWelcome = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
            startActivity(intentWelcome);
            finish();
        }
    }
}