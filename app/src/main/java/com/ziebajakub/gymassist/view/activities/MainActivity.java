package com.ziebajakub.gymassist.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.ziebajakub.gymassist.R;
import com.ziebajakub.gymassist.databinding.ActivityMainBinding;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.services.models.Workout;
import com.ziebajakub.gymassist.view.fragments.ProfileFragment;
import com.ziebajakub.gymassist.view.fragments.WorkoutFragment;
import com.ziebajakub.gymassist.view.interfaces.Constants;
import com.ziebajakub.gymassist.view.interfaces.NavigationListener;
import com.ziebajakub.gymassist.viewmodels.AuthViewModel;
import com.ziebajakub.gymassist.viewmodels.WorkoutViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationListener {

    private ActivityMainBinding binding;
    private AuthViewModel authViewModel;
    private WorkoutViewModel workoutViewModel;
    private User user;
    private List<Workout> workouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getBundles();
        initViewModels();
        observeUser();
        observeWorkouts();
        setListeners();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                WorkoutFragment.newInstance(user, workouts)).commit();

    }

    @SuppressWarnings("unchecked")
    private void getBundles() {
        if (getIntent().getExtras() != null) {
            user = (User) getIntent().getExtras().getSerializable(Constants.USER);
            workouts = (List<Workout>) getIntent().getExtras().getSerializable(Constants.WORKOUTS);
        }
    }

    private void initViewModels() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
    }

    private void setListeners() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = new Fragment();
            switch (item.getItemId()) {
                case R.id.nav_workout:
                    selectedFragment = WorkoutFragment.newInstance(user, workouts);
                    break;
                case R.id.nav_profile:
                    selectedFragment = ProfileFragment.newInstance(user);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        });
    }

    private void observeUser() {
        authViewModel.getUserLiveData().observe(this, user -> {
            if (user == null) {
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                changeToLoginView();
            }
        });
    }

    private void observeWorkouts() {
        workoutViewModel.getWorkoutsLiveData().observe(this, workoutsData -> {
            if (workoutsData == null) {
                Toast.makeText(this, "Error while loading workouts from database!", Toast.LENGTH_SHORT).show();
                changeToLoginView();
            }
        });
    }

    public void changeToLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void changeFragment(Fragment previous, Fragment next, Boolean addToBackStack) {
        if (previous.getView() != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, next);
            if (addToBackStack)
                fragmentTransaction.addToBackStack(next.getClass().getName());
            fragmentTransaction.commit();
        }
    }
}
