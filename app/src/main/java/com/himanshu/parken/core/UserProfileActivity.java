package com.himanshu.parken.core;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.parken.R;
import com.himanshu.parken.User;

public class UserProfileActivity extends AppCompatActivity {

    private DatabaseReference userDataReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userDataReference = FirebaseDatabase.getInstance().getReference("Users");
        String userId = firebaseUser.getUid();

        userDataReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if(user != null){
                    showUserProfile(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showUserProfile(User user){

        EditText etFirstName = findViewById(R.id.editText_user_profile_first_name);
        EditText etLastName = findViewById(R.id.editText_user_profile_last_name);
        EditText etEmail = findViewById(R.id.editText_user_profile_email);
        EditText etPhone = findViewById(R.id.editText_user_profile_phone);

        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());

        String gender = user.getGender();

        if(gender.equalsIgnoreCase("male")){
            findViewById(R.id.radioButton_user_profile_gender_male).setSelected(true);
        }
        else if(gender.equalsIgnoreCase("female")){
            findViewById(R.id.radioButton_user_profile_gender_female).setSelected(true);
        }
        else{
            findViewById(R.id.radioButton_user_profile_gender_other).setSelected(true);
        }

    }

}