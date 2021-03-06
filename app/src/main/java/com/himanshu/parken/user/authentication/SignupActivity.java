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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.himanshu.parken.R;
import com.himanshu.parken.user.User;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Button btSignup = findViewById(R.id.button_signup_signup);

        btSignup.setOnClickListener(view -> signUpUser());

    }

    public void signUpUser() {


        EditText etFirstName = findViewById(R.id.editText_signup_first_name);
        EditText etLastName = findViewById(R.id.editText_signup_last_name);
        EditText etEmail = findViewById(R.id.textInputEditText_signup_email);
        EditText etPassword = findViewById(R.id.textInputEditText_signup_password);
        EditText etConfirmPassword = findViewById(R.id.textInputEditText_signup_confirm_password);
        EditText etPhone = findViewById(R.id.textInputEditText_signup_phone);

        RadioGroup rgGender = findViewById(R.id.radioGroup_signup_gender);
        progressBar = findViewById(R.id.progressBar_signup);
        mAuth = FirebaseAuth.getInstance();

        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String phone = etPhone.getText().toString();
        String gender;

        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        RadioButton rbGender = findViewById(selectedGenderId);

        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError("First name required!");
            etFirstName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            etLastName.setError("Last name required!");
            etLastName.requestFocus();
            return;
        }
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
        if (password.length() < 6) {
            etPassword.setError("Password length must be at least 6!");
            etPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm password required!");
            etConfirmPassword.requestFocus();
            return;
        }
        if (!TextUtils.equals(password, confirmPassword)) {
            etConfirmPassword.setError("Confirm password not matching with password!");
            etConfirmPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number required!");
            etPhone.requestFocus();
            return;
        }
        if (selectedGenderId == -1) {
            TextInputLayout tilGender = findViewById(R.id.textInputLayout_signup_gender);
            tilGender.setError("Gender is required!");
            return;
        }
        gender = rbGender.getText().toString();
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, taskCreateUser -> {

                    if (taskCreateUser.isSuccessful()) {
                        User user = new User(firstName, lastName, email, gender, phone, password);

                        FirebaseDatabase.getInstance().getReference("User")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(user).addOnCompleteListener(taskAddUserData -> {
                                    if (taskAddUserData.isSuccessful()) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        FirebaseUser fbUser = mAuth.getCurrentUser();
                                        Objects.requireNonNull(fbUser).sendEmailVerification();
                                        Toast.makeText(this, "User created successfully" + "\nAn email verification is sent to you.", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            throw Objects.requireNonNull(taskCreateUser.getException());
                        } catch (FirebaseAuthUserCollisionException existEmail) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            builder.setTitle("User already exists");
                            builder.setMessage(existEmail.getMessage())
                                    .setCancelable(false)
                                    .setNeutralButton(android.R.string.ok, (dialog, id) -> dialog.dismiss());
                            AlertDialog alert = builder.create();
                            alert.show();

                        } catch (Exception e) {
                            Toast.makeText(this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });


    }

}