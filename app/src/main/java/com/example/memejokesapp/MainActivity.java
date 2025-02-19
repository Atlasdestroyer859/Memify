package com.example.memejokesapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.MemeJokesApp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Set default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new MemeFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_meme) {
                selectedFragment = new MemeFragment();
            } else if (item.getItemId() == R.id.nav_jokes) {
                selectedFragment = new JokesFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, selectedFragment).commit();
            return true;
        });
    }
}