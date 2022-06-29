package com.himanshu.parken.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.parken.R;
import com.himanshu.parken.user.authentication.LoginActivity;

public class UserProfileActivity extends AppCompatActivity {

    private DatabaseReference userDataReference;
    private FirebaseUser firebaseUser;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userDataReference = FirebaseDatabase.getInstance().getReference("User");

        String userId = firebaseUser.getUid();
        String userIdText = "User ID: " + userId;
        TextView tvUserId = findViewById(R.id.textView_user_profile_user_id);
        tvUserId.setText(userIdText);
        tvUserId.setOnClickListener(view -> {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("UserId", userId);
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(UserProfileActivity.this, "User ID copied.", Toast.LENGTH_SHORT).show();
        });

        progress = new ProgressDialog(this);
        progress.setMessage("Fetching Data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);

        fetchUserDataAndShow();

    }

    private void fetchUserDataAndShow() {
        progress.show();
        userDataReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null) {
                    showUserProfile(user);
                }
                else{
                    progress.dismiss();
                    Toast.makeText(UserProfileActivity.this, "some error occurred while fetching user data!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progress.dismiss();
                Toast.makeText(UserProfileActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_user_profile, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItem_user_refresh:
                refreshUserProfile();
                return true;

            case R.id.menuItem_user_signout:
                signOutUser();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showUserProfile(@NonNull User user) {

        progress.dismiss();

        EditText etFirstName = findViewById(R.id.textInputEditText_user_profile_first_name);
        EditText etLastName = findViewById(R.id.textInputEditText_user_profile_last_name);
        EditText etEmail = findViewById(R.id.textInputEditText_user_profile_email);
        EditText etPhone = findViewById(R.id.textInputEditText_user_profile_phone);

        RadioGroup rgGender = findViewById(R.id.radioGroup_user_profile_gender);

        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());

        String gender = user.getGender();

        if (gender.equalsIgnoreCase("male")) {
            rgGender.check(R.id.radioButton_user_profile_gender_male);
        } else if (gender.equalsIgnoreCase("female")) {
            rgGender.check(R.id.radioButton_user_profile_gender_female);
        } else {
            rgGender.check(R.id.radioButton_user_profile_gender_other);
        }

    }

    public void refreshUserProfile() {
        fetchUserDataAndShow();
    }

    public void signOutUser(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}