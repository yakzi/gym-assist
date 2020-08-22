package com.ziebajakub.gymassist.services.models;

import com.ziebajakub.gymassist.view.enums.DayOfWeek;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Workout implements Serializable {

    private int id;
    private DayOfWeek day;
    private List<Exercise> exercises;

    public Workout() {
    }

    public Workout(int id, DayOfWeek day) {
        this.id = id;
        this.day = day;
        this.exercises = new ArrayList<>();
    }

    public int getId() {
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
}
