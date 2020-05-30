package com.ziebajakub.gymassist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ziebajakub.gymassist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initAuthViewModel();
        //observeUser();
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

    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void observeUser(){
//        authViewModel.getUserLiveData().observe(this, user -> {
//            if (user == null){
//                changeToLoginView();
//            }
//        });
    }

    public void changeToLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
