package com.himanshu.parken;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword, etPhone;
    private ProgressBar progressBar;
    private RadioGroup rgGender;
    private RadioButton rbGender;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Objects.requireNonNull(getSupportActionBar()).hide();

        etFirstName = findViewById(R.id.editText_signup_first_name);
        etLastName = findViewById(R.id.editText_signup_last_name);
        etEmail = findViewById(R.id.editText_signup_email);
        etPassword = findViewById(R.id.editText_signup_password);
        etConfirmPassword = findViewById(R.id.editText_signup_confirm_password);
        etPhone = findViewById(R.id.editText_signup_phone);

        rgGender = findViewById(R.id.radioGroup_signup_gender);
        rgGender.clearCheck();
        progressBar = findViewById(R.id.progressBar_signup);
        mAuth = FirebaseAuth.getInstance();

        Button btSignup = findViewById(R.id.button_signup_signup);

        btSignup.setOnClickListener(view -> signUpUser());

    }

    public void signUpUser() {

        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String phone = etPhone.getText().toString();
        String gender;

        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        rbGender = findViewById(selectedGenderId);

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
        if(password.length() < 6){
            etPassword.setError("Password length must be at least 6!");
            etPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm password required!");
            etConfirmPassword.requestFocus();
            return;
        }
        if(!TextUtils.equals(password, confirmPassword)){
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
            Toast.makeText(SignupActivity.this, "Gender is required!", Toast.LENGTH_SHORT).show();
        } else {
            gender = rbGender.getText().toString();
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, task -> {
                        if (task.isSuccessful()) {
                            User user = new User(firstName, lastName, email, gender, phone, password);
                            FirebaseUser fbUser = mAuth.getCurrentUser();
                            //fbUser.sendEmailVerification();

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    });
        }

    }

}