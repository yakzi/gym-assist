package com.ziebajakub.gymassist.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ziebajakub.gymassist.R;
import com.ziebajakub.gymassist.databinding.FragmentExerciseBinding;
import com.ziebajakub.gymassist.services.models.Exercise;
import com.ziebajakub.gymassist.services.models.Workout;
import com.ziebajakub.gymassist.view.adapters.HistoryAdapter;
import com.ziebajakub.gymassist.view.interfaces.Constants;
import com.ziebajakub.gymassist.viewmodels.ExerciseViewModel;
import com.ziebajakub.gymassist.viewmodels.WorkoutViewModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ExerciseFragment extends BaseFragment implements View.OnClickListener {

    private FragmentExerciseBinding binding;
    private ExerciseViewModel exerciseViewModel;
    private WorkoutViewModel workoutViewModel;
    private List<Workout> workouts;
    private Exercise exercise;
    private Workout workout;
    private HistoryAdapter adapter;

    public ExerciseFragment() {
    }

    public static ExerciseFragment newInstance(Exercise exercise, Workout workout, List<Workout> workouts) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.EXERCISE, exercise);
        args.putSerializable(Constants.WORKOUT, workout);
        args.putSerializable(Constants.WORKOUTS, (Serializable) workouts);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exercise = (Exercise) getArguments().getSerializable(Constants.EXERCISE);
            workout = (Workout) getArguments().getSerializable(Constants.WORKOUT);
            workouts = (List<Workout>) getArguments().getSerializable(Constants.WORKOUTS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExerciseBinding.inflate(inflater, container, false);

        initViewModel();
        initView();
        setListeners();

        return binding.getRoot();
    }

    private void initViewModel() {
        if (getActivity() != null) {
            exerciseViewModel = new ViewModelProvider(getActivity()).get(ExerciseViewModel.class);
            exerciseViewModel.getExerciseLiveData().observe(getViewLifecycleOwner(), exercise -> {
                if (exercise != null) {
                    this.exercise = exercise;
                }
            });
        }
    }

    private void setListeners() {
        binding.exerciseAddHistoryButton.setOnClickListener(this);
        binding.exerciseDeleteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exercise_delete_button:
                //todo delete
                break;
            case R.id.exercise_add_history_button:
                //todo add
        }
    }

    private void initView() {
        if (exercise != null && getContext() != null) {
            setListAdapter();
            binding.exerciseName.setText(exercise.getName());
        } else {
            getNavigation().back();
        }
    }

    private void setListAdapter() {
        binding.exerciseHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryAdapter(getContext(), exercise.getReps(), exercise.getSets(), exercise.getWeights());
        binding.exerciseHistoryList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
