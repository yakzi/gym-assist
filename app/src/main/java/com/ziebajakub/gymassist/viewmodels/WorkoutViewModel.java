package com.ziebajakub.gymassist.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ziebajakub.gymassist.services.models.Workout;
import com.ziebajakub.gymassist.services.repositories.WorkoutRepository;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {
    private WorkoutRepository workoutRepository;
    private MutableLiveData<Workout> workoutLiveData;
    private MutableLiveData<List<Workout>> workoutsLiveData;

    public WorkoutViewModel(Application application) {
        super(application);
        workoutRepository = new WorkoutRepository();
        workoutLiveData = new MutableLiveData<>();
        workoutsLiveData = new MutableLiveData<>();
    }

    public LiveData<Workout> getWorkoutLiveData() {
        return workoutLiveData;
    }

    public LiveData<List<Workout>> getWorkoutsLiveData() {
        return workoutsLiveData;
    }

    public String generateId() {
        return workoutRepository.generateId();
    }

    public void addWorkout(Workout workout) {
        workoutLiveData = workoutRepository.addWorkout(workout);
    }

    public void getWorkout(String id) {
        workoutLiveData = workoutRepository.getWorkout(id);
    }

    public void getWorkouts(List<String> ids) {
        workoutsLiveData = workoutRepository.getWorkouts(ids);
    }
}
