package com.ziebajakub.gymassist.services.models;

import androidx.annotation.NonNull;

import com.ziebajakub.gymassist.view.enums.DayOfWeek;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Workout implements Serializable, Comparable<Workout> {

    private String id;
    private DayOfWeek day;
    private List<Exercise> exercises;

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

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    @Override
    public int compareTo(@NonNull Workout w) {
        return Integer.compare(day.ordinal(), w.day.ordinal());
    }
}
