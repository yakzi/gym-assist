package com.ziebajakub.gymassist.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ziebajakub.gymassist.R;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.services.models.Workout;
import com.ziebajakub.gymassist.view.interfaces.Constants;
import com.ziebajakub.gymassist.viewmodels.AuthViewModel;
import com.ziebajakub.gymassist.viewmodels.WorkoutViewModel;

import java.io.Serializable;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private WorkoutViewModel workoutViewModel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getBundles();
        initViewModels();
        checkUser();
    }

    private void getBundles() {
        if (getIntent().getExtras() != null) {
            user = (User) getIntent().getExtras().getSerializable(Constants.USER);
        }
    }

    private void initViewModels() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
    }

    private void changeToActivity(Activity activity, @Nullable User user, @Nullable List<Workout> workouts) {
        Intent intent = new Intent(this, activity.getClass());
        if (user != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.USER, user);
            bundle.putSerializable(Constants.WORKOUTS, (Serializable) workouts);
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    private void checkUser() {
        if (user == null) {
            if (authViewModel.isLoggedUser()) {
                getUserFromDatabase();
            } else {
                changeToActivity(new LoginActivity(), null, null);
            }
        } else {
            getUserWorkouts(user);
        }
    }


    private void getUserFromDatabase() {
        authViewModel.getUserFromDatabase(authViewModel.getLoggedUserId());
        authViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                getUserWorkouts(user);
            } else {
                changeToActivity(new LoginActivity(), null, null);
            }
        });
    }

    private void getUserWorkouts(User user) {
        workoutViewModel.getWorkouts(user.getWorkouts());
        workoutViewModel.getWorkoutsLiveData().observe(this, workouts -> {
            if (workouts != null) {
                changeToActivity(new MainActivity(), user, workouts);
            } else {
                changeToActivity(new MainActivity(), user, null);
            }
        });
    }

}
