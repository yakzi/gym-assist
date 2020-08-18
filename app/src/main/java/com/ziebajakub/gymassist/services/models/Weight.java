package com.ziebajakub.gymassist.services.models;

import java.io.Serializable;

public class Weight implements Serializable {

    private double value;
    private long date;

    public Weight() {
    }

    public Weight(double value, long date) {
        this.value = value;
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public long getDate() {
        return date;
    }
}
