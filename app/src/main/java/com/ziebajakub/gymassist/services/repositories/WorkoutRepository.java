package com.ziebajakub.gymassist.services.repositories;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ziebajakub.gymassist.services.models.Workout;
import com.ziebajakub.gymassist.view.interfaces.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkoutRepository {
    private CollectionReference workoutsRef;

    public WorkoutRepository() {
        workoutsRef = FirebaseFirestore.getInstance().collection(Constants.DB_WORKOUTS);
    }

    public String generateId() {
        return workoutsRef.document().getId();
    }

    public MutableLiveData<Workout> addWorkout(Workout workout) {
        MutableLiveData<Workout> workoutMutableLiveData = new MutableLiveData<>();
        DocumentReference workoutRef = workoutsRef.document(workout.getId());
        workoutRef.set(workout).addOnCompleteListener(task ->
                workoutMutableLiveData.setValue(task.isSuccessful() ? workout : null));
        return workoutMutableLiveData;
    }

    public MutableLiveData<Workout> getWorkout(String id) {
        MutableLiveData<Workout> workoutMutableLiveData = new MutableLiveData<>();
        DocumentReference workoutRef = workoutsRef.document(id);
        workoutRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Workout workout = documentSnapshot.toObject(Workout.class);
                    workoutMutableLiveData.setValue(workout);
                } else {
                    workoutMutableLiveData.setValue(null);
                }
            } else {
                workoutMutableLiveData.setValue(null);
            }
        });
        return workoutMutableLiveData;
    }

    public MutableLiveData<List<Workout>> getWorkouts(List<String> ids) {
        MutableLiveData<List<Workout>> workoutsData = new MutableLiveData<>();
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (String id : ids)
            if (id != null)
                tasks.add(workoutsRef.document(id).get());
        Task<List<DocumentSnapshot>> finalTask = Tasks.whenAllSuccess(tasks);
        finalTask.addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Workout> workouts = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : task.getResult())
                    workouts.add(documentSnapshot.toObject(Workout.class));
                workoutsData.setValue(workouts);
            }
        });
        return workoutsData;
    }

    public void addExercise(String id, HashMap<String, Object> changes) {
        workoutsRef.document(id).update(changes);
    }
}
