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

    public Exercise(String id, String name, History rep, History set, History weight) {
        this.id = id;
        this.name = name;
        this.reps = new ArrayList<>();
        this.sets = new ArrayList<>();
        this.weights = new ArrayList<>();

        this.reps.add(rep);
        this.sets.add(set);
        this.weights.add(weight);
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

    public History getNewestRep() {
        return reps.size() > 0 ? reps.get(reps.size() - 1) : null;
    }

    public History getNewestSet() {
        return sets.size() > 0 ? sets.get(sets.size() - 1) : null;
    }

    public History getNewestWeight() {
        return weights.size() > 0 ? weights.get(weights.size() - 1) : null;
    }
}
