package com.ziebajakub.gymassist.view.fragments;

import android.annotation.SuppressLint;
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
import com.ziebajakub.gymassist.databinding.DialogAddProgressBinding;
import com.ziebajakub.gymassist.databinding.DialogDeleteExerciseBinding;
import com.ziebajakub.gymassist.databinding.FragmentExerciseBinding;
import com.ziebajakub.gymassist.services.models.Exercise;
import com.ziebajakub.gymassist.services.models.History;
import com.ziebajakub.gymassist.services.models.Workout;
import com.ziebajakub.gymassist.view.adapters.HistoryAdapter;
import com.ziebajakub.gymassist.view.interfaces.Constants;
import com.ziebajakub.gymassist.viewmodels.ExerciseViewModel;
import com.ziebajakub.gymassist.viewmodels.WorkoutViewModel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.ziebajakub.gymassist.view.fragments.WorkoutFragment.validateInput;
import static com.ziebajakub.gymassist.view.interfaces.Constants.ERROR_INPUT;

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

    static ExerciseFragment newInstance(Exercise exercise, Workout workout, List<Workout> workouts) {
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

        initViewModels();
        initView();
        setListeners();

        return binding.getRoot();
    }

    private void initViewModels() {
        if (getActivity() != null) {
            workoutViewModel = new ViewModelProvider(getActivity()).get(WorkoutViewModel.class);
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
                openDeleteDialog();
                break;
            case R.id.exercise_add_history_button:
                openAddDialog();
        }
    }

    @SuppressLint("SetTextI18n")
    private void openAddDialog() {
        if (getContext() != null) {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            DialogAddProgressBinding binding = DialogAddProgressBinding.inflate(LayoutInflater.from(getContext()));
            dialog.setContentView(binding.getRoot());
            binding.dialogAddProgressRepsInput.setText((int) exercise.getNewestRep().getValue() + "");
            binding.dialogAddProgressSetsInput.setText((int) exercise.getNewestSet().getValue() + "");
            binding.dialogAddProgressWeightInput.setText(exercise.getNewestWeight().getValue() + "");
            binding.dialogAddProgressButtonAdd.setOnClickListener(view -> {
                if (validateInput(binding.dialogAddProgressRepsInput, true)
                        && validateInput(binding.dialogAddProgressSetsInput, true)
                        && validateInput(binding.dialogAddProgressWeightInput, false)) {
                    Editable editReps = binding.dialogAddProgressRepsInput.getText();
                    Editable editSets = binding.dialogAddProgressSetsInput.getText();
                    Editable editWeight = binding.dialogAddProgressWeightInput.getText();
                    assert editReps != null;
                    assert editSets != null;
                    assert editWeight != null;
                    if (!editReps.toString().equals(String.valueOf((int) exercise.getNewestRep().getValue()))
                            || !editSets.toString().equals(String.valueOf((int) exercise.getNewestSet().getValue()))
                            || !editWeight.toString().equals(String.valueOf(exercise.getNewestWeight().getValue()))) {
                        addProgress(editReps.toString(), editSets.toString(), editWeight.toString());
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(getContext(), ERROR_INPUT, Toast.LENGTH_SHORT).show();
                }
            });
            binding.dialogAddProgressButtonCancel.setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }
    }

    private void addProgress(String newRep, String newSet, String newWeight) {
        long now = Calendar.getInstance().getTimeInMillis();
        binding.exerciseHistoryList.scrollToPosition(0);
        adapter.add(new History(Double.parseDouble(newRep), now),
                new History(Double.parseDouble(newSet), now), new History(Double.parseDouble(newWeight), now));
        WorkoutFragment.adapter.notifyDataSetChanged();
        exerciseViewModel.updateExercise(exercise.getId(), new HashMap<String, Object>() {{
            put(Constants.DB_REPS, exercise.getReps());
            put(Constants.DB_SETS, exercise.getSets());
            put(Constants.DB_WEIGHTS, exercise.getWeights());
        }});
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

    private void openDeleteDialog() {
        if (getContext() != null) {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            DialogDeleteExerciseBinding binding = DialogDeleteExerciseBinding.inflate(LayoutInflater.from(getContext()));
            dialog.setContentView(binding.getRoot());
            binding.dialogDeleteExerciseButtonConfirm.setOnClickListener(view -> {
                deleteExercise();
                dialog.dismiss();
            });
            binding.dialogDeleteExerciseButtonCancel.setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }
    }

    private void deleteExercise() {
        workouts.get(workout.getDay().ordinal()).getExercisesData().remove(exercise);
        workouts.get(workout.getDay().ordinal()).getExercises().remove(exercise.getId());
        workoutViewModel.updateExercise(workout.getId(), new HashMap<String, Object>() {{
            put(Constants.DB_EXERCISES, workout.getExercises());
        }});
        exerciseViewModel.removeExercise(exercise.getId());
        Toast.makeText(getContext(), "Successfully deleted exercise.", Toast.LENGTH_SHORT).show();
        getNavigation().back();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
