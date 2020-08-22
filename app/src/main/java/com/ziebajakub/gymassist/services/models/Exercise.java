package com.ziebajakub.gymassist.services.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Exercise implements Serializable {

    private String id;
    private String name;
    private List<History> reps;
    private List<History> sets;
    private List<History> weights;

    public Exercise() {
    }

    public Exercise(String id, String name, int rep, int set, int weight) {
        this.id = id;
        this.name = name;
        this.reps = new ArrayList<>(rep);
        this.sets = new ArrayList<>(set);
        this.weights = new ArrayList<>(weight);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<History> getReps() {
        return reps;
    }

    public List<History> getSets() {
        return sets;
    }

    public List<History> getWeights() {
        return weights;
    }
}
