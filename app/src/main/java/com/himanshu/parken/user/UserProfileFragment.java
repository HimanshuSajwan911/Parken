package com.himanshu.parken.user;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class UserProfileFragment extends Fragment {

    private EditText etFirstName, etLastName, etEmail, etPhone, etGender;

    private DatabaseReference userDataReference;
    private FirebaseUser firebaseUser;
    private ProgressDialog progress;


    public UserProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);


        etFirstName = view.findViewById(R.id.textInputEditText_fragment_user_profile_first_name);
        etLastName = view.findViewById(R.id.textInputEditText_fragment_user_profile_last_name);
        etEmail = view.findViewById(R.id.textInputEditText_fragment_user_profile_email);
        etPhone = view.findViewById(R.id.textInputEditText_fragment_user_profile_phone);
        etGender = view.findViewById(R.id.textInputEditText_fragment_user_profile_gender);

        etFirstName.setKeyListener(null);
        etLastName.setKeyListener(null);
        etEmail.setKeyListener(null);
        etPhone.setKeyListener(null);
        etGender.setKeyListener(null);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userDataReference = FirebaseDatabase.getInstance().getReference("Users");

        String userId = firebaseUser.getUid();
        String userIdText = "User ID: " + userId;
        TextView tvUserId = view.findViewById(R.id.textView_fragment_user_profile_user_id);
        tvUserId.setText(userIdText);
        tvUserId.setOnClickListener(viewUserId -> {
            FragmentActivity fragmentActivity = getActivity();
            if(fragmentActivity != null){
                ClipboardManager clipboardManager = (ClipboardManager) fragmentActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ParkenUserId", userId);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getActivity(), "User ID copied.", Toast.LENGTH_SHORT).show();
            }
        });

        progress = new ProgressDialog(getActivity());
        progress.setMessage("Fetching Data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);

        fetchUserDataAndShow();

        return view;
    }

    private void fetchUserDataAndShow() {
        progress.show();
        userDataReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    showUserProfile(user);
                } else {
                    Toast.makeText(getActivity(), "some error occurred while fetching user data!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showUserProfile(@NonNull User user) {

        progress.dismiss();

        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());
        etGender.setText(user.getGender());

    }

}