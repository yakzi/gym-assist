package com.ziebajakub.gymassist.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ziebajakub.gymassist.R;
import com.ziebajakub.gymassist.databinding.DialogAddWeightBinding;
import com.ziebajakub.gymassist.databinding.FragmentWeightHistoryBinding;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.services.models.Weight;
import com.ziebajakub.gymassist.view.adapters.WeightAdapter;
import com.ziebajakub.gymassist.view.interfaces.Constants;
import com.ziebajakub.gymassist.viewmodels.AuthViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WeightHistoryFragment extends BaseFragment implements View.OnClickListener {

    private FragmentWeightHistoryBinding binding;
    private AuthViewModel authViewModel;
    private User user;
    private WeightAdapter adapter;

    public WeightHistoryFragment() {
    }

    public static WeightHistoryFragment newInstance(User user) {
        WeightHistoryFragment fragment = new WeightHistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(Constants.USER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWeightHistoryBinding.inflate(inflater, container, false);

        initAuthViewModel();
        initList();
        setListeners();

        return binding.getRoot();
    }

    private void initAuthViewModel() {
        if (getActivity() != null) {
            authViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);
        }
    }

    private void setListeners() {
        binding.weightHistoryAddButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.weight_history_add_button:
                openWeightDialog();
        }
    }

    private void initList() {
        if (user != null && getContext() != null) {
            binding.weightHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
            List<Weight> weights = new ArrayList<>(user.getWeightHistory());
            adapter = new WeightAdapter(getContext(), weights);
            binding.weightHistoryList.setAdapter(adapter);
        }
    }

    private void openWeightDialog() {
        if (getContext() != null) {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            DialogAddWeightBinding binding = DialogAddWeightBinding.inflate(LayoutInflater.from(getContext()));
            dialog.setContentView(binding.getRoot());
            binding.dialogAddWeightButtonAdd.setOnClickListener(view -> {
                Editable inputValue = binding.dialogAddWeightInput.getText();
                if (inputValue != null && validateWeightInput(inputValue.toString())) {
                    addWeight(binding.dialogAddWeightInput.getText().toString());
                    dialog.dismiss();
                }
            });
            binding.dialogAddWeightButtonCancel.setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }
    }

    private void addWeight(String weight) {
        Weight newWeight = new Weight(Double.parseDouble(weight), Calendar.getInstance().getTimeInMillis());
        user.getWeightHistory().add(newWeight);
        authViewModel.updateUserData(user.getUid(), new HashMap<String, Object>() {{
            put(Constants.DB_WEIGHTS, user.getWeightHistory());
        }});
        adapter.add(newWeight);
    }

    private boolean validateWeightInput(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: Invalid weight value!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
