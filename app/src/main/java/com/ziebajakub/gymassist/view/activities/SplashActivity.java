package com.ziebajakub.gymassist.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ziebajakub.gymassist.R;
import com.ziebajakub.gymassist.services.models.Exercise;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.services.models.Workout;
import com.ziebajakub.gymassist.view.interfaces.Constants;
import com.ziebajakub.gymassist.viewmodels.AuthViewModel;
import com.ziebajakub.gymassist.viewmodels.ExerciseViewModel;
import com.ziebajakub.gymassist.viewmodels.WorkoutViewModel;

import java.io.Serializable;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private User user;

    private MutableLiveData<Integer> loadedExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadedExercises = new MutableLiveData<>(0);
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
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
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
                getWorkoutsExercises(user, workouts);
            } else {
                changeToActivity(new MainActivity(), user, null);
            }
        });
    }

    private void getWorkoutsExercises(User user, List<Workout> workouts) {
        for(int i = 0; i < workouts.size(); i++) {
            exerciseViewModel.getExercises(workouts.get(i).getExercises());
            int finalI = i;
            exerciseViewModel.getExercisesLiveData().observe(this, exercises -> {
                if (exercises != null) {
                    workouts.get(finalI).addExercisesData(exercises);
                }
                loadedExercises.setValue(loadedExercises.getValue() != null ? loadedExercises.getValue() + 1 : 0);
            });
        }
        loadedExercises.observe(this, value -> {
            if (value == workouts.size()) {
                changeToActivity(new MainActivity(), user, workouts);
            }
        });
    }

}
