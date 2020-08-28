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
        Collections.reverse(this.reps);
        Collections.reverse(this.sets);
        Collections.reverse(this.weights);
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

    @SuppressLint("SimpleDateFormat")
    private String getTimeFromMillis(long millis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(millis);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");
        return format.format(date.getTime());
    }

    @Override
    public int getItemCount() {
        return reps.size();
    }

    static class HistoryHolder extends RecyclerView.ViewHolder {
        private ItemHistoryBinding binding;

        HistoryHolder(ItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}