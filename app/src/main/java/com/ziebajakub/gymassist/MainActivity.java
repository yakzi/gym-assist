package com.ziebajakub.gymassist;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ziebajakub.gymassist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setListeners();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkoutFragment()).commit();

    }

    private void setListeners() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = new Fragment();

            switch (item.getItemId()) {
                case R.id.nav_workout:
                    selectedFragment = new WorkoutFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });
    }

}
