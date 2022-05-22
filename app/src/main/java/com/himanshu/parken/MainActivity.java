package com.himanshu.parken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.himanshu.parken.core.HomeFragment;
import com.himanshu.parken.core.SearchFragment;
import com.himanshu.parken.user.UserProfileFragment;

public class MainActivity extends AppCompatActivity {

    private static final String ROOT_FRAGMENT_TAG = "HOME_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bnvMain = findViewById(R.id.bottomNavigationView_main);

        bnvMain.setSelectedItemId(R.id.nav_main_home);
        loadFragment(new HomeFragment(), true);

        bnvMain.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_main_home) {
                loadFragment(new HomeFragment(), true);
            } else if (id == R.id.nav_main_search) {
                loadFragment(new SearchFragment(), false);
            } else if (id == R.id.nav_main_profile) {
                loadFragment(new UserProfileFragment(), false);
            }
            return true;
        });

    }

    public void loadFragment(Fragment fragment, boolean add) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (add) {
            fragmentTransaction.add(R.id.frameLayout_main, fragment);
            fragmentManager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.addToBackStack(ROOT_FRAGMENT_TAG);
        } else {
            fragmentTransaction.replace(R.id.frameLayout_main, fragment);
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }


}