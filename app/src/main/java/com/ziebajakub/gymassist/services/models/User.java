package com.ziebajakub.gymassist.services.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String uid;
    private String name;
    private String email;
    private List<History> weights;
    private List<String> workouts;
    @Exclude
    private boolean isNew;

    public User() {
    }

    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.weights = new ArrayList<>();
        this.workouts = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    @Exclude
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public List<History> getWeights() {
        return weights;
    }

    public List<String> getWorkouts() {
        return workouts;
    }

    public void addWorkout(String id) {
        this.workouts.add(id);
    }
}