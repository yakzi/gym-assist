package com.ziebajakub.gymassist.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziebajakub.gymassist.databinding.ItemWeightBinding;
import com.ziebajakub.gymassist.services.models.History;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.WeightHolder> {

    private Context context;
    private List<History> weights;

    public WeightAdapter(Context context, List<History> weights) {
        this.context = context;
        this.weights = weights;
        Collections.reverse(this.weights);
    }

    @NonNull
    @Override
    public WeightHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWeightBinding binding = ItemWeightBinding.inflate(LayoutInflater.from(context), parent, false);
        return new WeightHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeightHolder holder, int position) {
        final History weight = weights.get(position);
        holder.binding.weightItemValue.setText(weight.getValue() + "");
        holder.binding.weightItemDate.setText(getTimeFromMillis(weight.getDate()));
    }

    @SuppressLint("SimpleDateFormat")
    private String getTimeFromMillis(long millis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(millis);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");
        return format.format(date.getTime());
    }

    public void add(History weight) {
        weights.add(0, weight);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, weights.size());
    }

    @Override
    public int getItemCount() {
        return weights.size();
    }

    static class WeightHolder extends RecyclerView.ViewHolder {
        private ItemWeightBinding binding;
        WeightHolder(ItemWeightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}