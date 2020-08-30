package com.ziebajakub.gymassist.services.repositories;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ziebajakub.gymassist.services.models.Exercise;
import com.ziebajakub.gymassist.view.interfaces.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExerciseRepository {
    private CollectionReference exercisesRef;

    public ExerciseRepository() {
        exercisesRef = FirebaseFirestore.getInstance().collection(Constants.DB_EXERCISES);
    }

    public String generateId() {
        return exercisesRef.document().getId();
    }

    public MutableLiveData<Exercise> addExercise(Exercise exercise) {
        MutableLiveData<Exercise> exerciseMutableLiveData = new MutableLiveData<>();
        DocumentReference exerciseRef = exercisesRef.document(exercise.getId());
        exerciseRef.set(exercise).addOnCompleteListener(task ->
                exerciseMutableLiveData.setValue(task.isSuccessful() ? exercise : null));
        return exerciseMutableLiveData;
    }

    public MutableLiveData<Exercise> getExercise(String id) {
        MutableLiveData<Exercise> exerciseMutableLiveData = new MutableLiveData<>();
        DocumentReference exerciseRef = exercisesRef.document(id);
        exerciseRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Exercise exercise = documentSnapshot.toObject(Exercise.class);
                    exerciseMutableLiveData.setValue(exercise);
                } else {
                    exerciseMutableLiveData.setValue(null);
                }
            } else {
                exerciseMutableLiveData.setValue(null);
            }
        });
        return exerciseMutableLiveData;
    }

    public MutableLiveData<List<Exercise>> getExercises(List<String> ids) {
        MutableLiveData<List<Exercise>> exercisesData = new MutableLiveData<>();
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (String id : ids)
            if (id != null)
                tasks.add(exercisesRef.document(id).get());
        Task<List<DocumentSnapshot>> finalTask = Tasks.whenAllSuccess(tasks);
        finalTask.addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Exercise> exercises = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : task.getResult())
                    exercises.add(documentSnapshot.toObject(Exercise.class));
                exercisesData.setValue(exercises);
            }
        });
        return exercisesData;
    }

    public void removeExercise(String id) {
        exercisesRef.document(id).delete();
    }

    public void updateExercise(String id, HashMap<String, Object> changes) {
        exercisesRef.document(id).update(changes);
    }
}
