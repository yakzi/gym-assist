package com.ziebajakub.gymassist.services.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;
import com.ziebajakub.gymassist.view.enums.DayOfWeek;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Workout implements Serializable, Comparable<Workout> {

    private String id;
    private DayOfWeek day;
    private List<String> exercises;

    @Exclude
    private List<Exercise> exercisesData;

    public Workout() {
    }

    public Workout(String id, DayOfWeek day) {
        this.id = id;
        this.day = day;
        this.exercises = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public List<String> getExercises() {
        return exercises;
    }

    public void addExercise(String id) {
        this.exercises.add(id);
    }

    @Exclude
    public void addExercisesData(List<Exercise> exercises) {
        this.exercisesData = exercises;
    }

    @Exclude
    public List<Exercise> getExercisesData() {
        return this.exercisesData;
    }

    @Override
    public int compareTo(@NonNull Workout w) {
        return Integer.compare(day.ordinal(), w.day.ordinal());
    }
}
