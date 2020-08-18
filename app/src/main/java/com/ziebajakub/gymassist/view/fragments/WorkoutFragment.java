package com.ziebajakub.gymassist.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ziebajakub.gymassist.R;
import com.ziebajakub.gymassist.databinding.FragmentWorkoutBinding;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.view.interfaces.Constants;

import java.util.Objects;

public class WorkoutFragment extends BaseFragment {

    private FragmentWorkoutBinding binding;
    private User user;

    public WorkoutFragment() {
    }

    public static WorkoutFragment newInstance(User user) {
        WorkoutFragment fragment = new WorkoutFragment();
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
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
