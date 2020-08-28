package com.ziebajakub.gymassist.services.models;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class History implements Serializable {

    private double value;
    private long date;

    public History() {
    }

    public History(double value, long date) {
        this.value = value;
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public long getDate() {
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public String getDateString() {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(this.date);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");
        return format.format(date.getTime());
    }
}
