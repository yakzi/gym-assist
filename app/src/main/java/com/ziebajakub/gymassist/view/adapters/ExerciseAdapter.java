package com.ziebajakub.gymassist.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziebajakub.gymassist.databinding.ItemExerciseBinding;
import com.ziebajakub.gymassist.databinding.ItemWeightBinding;
import com.ziebajakub.gymassist.services.models.Exercise;
import com.ziebajakub.gymassist.services.models.History;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {

    private Context context;
    private List<Exercise> exercises;

    public ExerciseAdapter(Context context, List<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExerciseBinding binding = ItemExerciseBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ExerciseHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
        final Exercise exercise = exercises.get(position);
        //todo set to holder
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class ExerciseHolder extends RecyclerView.ViewHolder {
        private ItemExerciseBinding binding;
        ExerciseHolder(ItemExerciseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}