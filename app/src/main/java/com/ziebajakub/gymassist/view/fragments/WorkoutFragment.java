package com.ziebajakub.gymassist.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.ziebajakub.gymassist.R;
import com.ziebajakub.gymassist.databinding.FragmentWorkoutBinding;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.services.models.Workout;
import com.ziebajakub.gymassist.view.enums.DayOfWeek;
import com.ziebajakub.gymassist.view.interfaces.Constants;
import com.ziebajakub.gymassist.viewmodels.WorkoutViewModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WorkoutFragment extends BaseFragment implements View.OnClickListener {

    private FragmentWorkoutBinding binding;
    private User user;
    private WorkoutViewModel workoutViewModel;
    private List<Workout> workoutList;
    private Workout currentWorkout;

    public WorkoutFragment() {
    }

    public static WorkoutFragment newInstance(User user, List<Workout> workouts) {
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.USER, user);
        args.putSerializable(Constants.WORKOUTS, (Serializable) workouts);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(Constants.USER);
            workoutList = (List<Workout>) getArguments().getSerializable(Constants.WORKOUTS);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);

        initViewModels();
        initView();
        setListeners();

        return binding.getRoot();
    }

    private void setListeners() {
        binding.workoutPrevDayButton.setOnClickListener(this);
        binding.workoutNextDayButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.workout_next_day_button:
                setCurrentWorkout(currentWorkout.getDay().ordinal() + 1);
                break;
            case R.id.workout_prev_day_button:
                setCurrentWorkout(currentWorkout.getDay().ordinal() - 1);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void initView() {
        Calendar now = Calendar.getInstance();
        binding.workoutDayDate.setText(new SimpleDateFormat("dd.MM.YYYY").format(now.getTime()));
        setCurrentWorkout(now.get(Calendar.DAY_OF_WEEK) - 2);
    }

    private void setCurrentWorkout(int day) {
        int dayValue = day >= 7 ? 0 : day < 0 ? 6 : day;
        currentWorkout = workoutList.get(dayValue);
        binding.workoutDayName.setText(currentWorkout.getDay().getName());
    }

    private void initViewModels() {
        if (getActivity() != null) {
            workoutViewModel = new ViewModelProvider(getActivity()).get(WorkoutViewModel.class);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
