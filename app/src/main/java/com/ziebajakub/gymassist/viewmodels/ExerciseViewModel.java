package com.ziebajakub.gymassist.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ziebajakub.gymassist.services.models.Exercise;
import com.ziebajakub.gymassist.services.repositories.ExerciseRepository;

import java.util.HashMap;
import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {
    private ExerciseRepository exerciseRepository;
    private MutableLiveData<Exercise> exerciseLiveData;
    private MutableLiveData<List<Exercise>> exercisesLiveData;

    public ExerciseViewModel(Application application) {
        super(application);
        exerciseRepository = new ExerciseRepository();
        exerciseLiveData = new MutableLiveData<>();
        exercisesLiveData = new MutableLiveData<>();
    }

    public LiveData<Exercise> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public LiveData<List<Exercise>> getExercisesLiveData() {
        return exercisesLiveData;
    }

    public String generateId() {
        return exerciseRepository.generateId();
    }

    public void addExercise(Exercise exercise) {
        exerciseLiveData = exerciseRepository.addExercise(exercise);
    }

    public void getExercise(String id) {
        exerciseLiveData = exerciseRepository.getExercise(id);
    }

    public void getExercises(List<String> ids) {
        exercisesLiveData = exerciseRepository.getExercises(ids);
    }

    public void removeExercise(String id) {
        exerciseRepository.removeExercise(id);
    }

    public void updateExercise(String id, HashMap<String, Object> changes) {
        exerciseRepository.updateExercise(id, changes);
    }
}
