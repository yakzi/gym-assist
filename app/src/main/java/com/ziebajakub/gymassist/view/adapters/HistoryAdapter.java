package com.ziebajakub.gymassist.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziebajakub.gymassist.databinding.ItemExerciseBinding;
import com.ziebajakub.gymassist.databinding.ItemHistoryBinding;
import com.ziebajakub.gymassist.services.models.Exercise;
import com.ziebajakub.gymassist.services.models.History;
import com.ziebajakub.gymassist.view.interfaces.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    private Context context;
    private List<History> reps;
    private List<History> sets;
    private List<History> weights;

    public HistoryAdapter(Context context, List<History> reps, List<History> sets, List<History> weights) {
        this.context = context;
        this.reps = reps;
        this.sets = sets;
        this.weights = weights;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding binding = ItemHistoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HistoryHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        final History rep = reps.get(position);
        final History set = sets.get(position);
        final History weight = weights.get(position);
        holder.binding.historyItemDate.setText(rep.getDateString());
        holder.binding.historyItemReps.setText((int) rep.getValue() + "");
        holder.binding.historyItemSets.setText((int) set.getValue() + "");
        holder.binding.historyItemWeight.setText(weight.getValue() > 0 ? weight.getValue() + "" : "-");
    }

    @Override
    public int getItemCount() {
        return reps.size();
    }

    public void add(History rep, History set, History weight) {
        weights.add(0, weight);
        reps.add(0, rep);
        sets.add(0, set);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, weights.size());
    }

    static class HistoryHolder extends RecyclerView.ViewHolder {
        private ItemHistoryBinding binding;

        HistoryHolder(ItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}