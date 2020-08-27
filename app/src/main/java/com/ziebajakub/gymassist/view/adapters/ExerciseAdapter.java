package com.ziebajakub.gymassist.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziebajakub.gymassist.databinding.ItemExerciseBinding;
import com.ziebajakub.gymassist.services.models.Exercise;
import com.ziebajakub.gymassist.services.models.History;
import com.ziebajakub.gymassist.view.interfaces.OnItemClickListener;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {

    private Context context;
    private List<Exercise> exercises;
    private OnItemClickListener onItemClickListener;

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
        holder.binding.exerciseItemName.setText(exercise.getName());
        holder.binding.exerciseItemReps.setText(exercise.getNewestRep() != null ? (int) exercise.getNewestRep().getValue() + "" : "-");
        holder.binding.exerciseItemSets.setText(exercise.getNewestSet() != null ? (int) exercise.getNewestSet().getValue() + "" : "-");
        holder.binding.exerciseItemWeight.setText(exercise.getNewestWeight() != null && exercise.getNewestWeight().getValue() > 0
                ? exercise.getNewestWeight().getValue() + "" : "-");
        holder.binding.exerciseItem.setOnClickListener(v -> onItemClickListener.onItemClick(exercise, position, v));
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
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