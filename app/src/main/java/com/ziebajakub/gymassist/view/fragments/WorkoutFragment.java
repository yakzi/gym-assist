package com.ziebajakub.gymassist.view.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;
import com.ziebajakub.gymassist.R;
import com.ziebajakub.gymassist.databinding.DialogAddExerciseBinding;
import com.ziebajakub.gymassist.databinding.FragmentWorkoutBinding;
import com.ziebajakub.gymassist.services.models.Exercise;
import com.ziebajakub.gymassist.services.models.History;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.services.models.Workout;
import com.ziebajakub.gymassist.view.adapters.ExerciseAdapter;
import com.ziebajakub.gymassist.view.interfaces.Constants;
import com.ziebajakub.gymassist.viewmodels.ExerciseViewModel;
import com.ziebajakub.gymassist.viewmodels.WorkoutViewModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.ziebajakub.gymassist.view.interfaces.Constants.ERROR_INPUT;

public class WorkoutFragment extends BaseFragment implements View.OnClickListener {

    private FragmentWorkoutBinding binding;
    private User user;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private List<Workout> workoutList;
    private Workout currentWorkout;
    private ExerciseAdapter adapter;

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

    private void initViewModels() {
        if (getActivity() != null) {
            workoutViewModel = new ViewModelProvider(getActivity()).get(WorkoutViewModel.class);
            exerciseViewModel = new ViewModelProvider(getActivity()).get(ExerciseViewModel.class);
        }
    }

    private void setListeners() {
        binding.workoutPrevDayButton.setOnClickListener(this);
        binding.workoutNextDayButton.setOnClickListener(this);
        binding.workoutAddExerciseButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.workout_next_day_button:
                setCurrentWorkout(currentWorkout.getDay().ordinal() + 1);
                break;
            case R.id.workout_prev_day_button:
                setCurrentWorkout(currentWorkout.getDay().ordinal() - 1);
                break;
            case R.id.workout_add_exercise_button:
                openAddExerciseDialog();
        }
    }

    private void openAddExerciseDialog() {
        if (getContext() != null) {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            DialogAddExerciseBinding binding = DialogAddExerciseBinding.inflate(LayoutInflater.from(getContext()));
            dialog.setContentView(binding.getRoot());
            binding.dialogAddExerciseButtonAdd.setOnClickListener(view -> {
                if (validateInput(binding.dialogAddExerciseNameInput, false)
                        && validateInput(binding.dialogAddExerciseRepsInput, true)
                        && validateInput(binding.dialogAddExerciseSetsInput, true)
                        && validateInput(binding.dialogAddExerciseWeightInput, false)) {
                    addExercise(createExercise(
                            Objects.requireNonNull(binding.dialogAddExerciseNameInput.getText()).toString(),
                            Objects.requireNonNull(binding.dialogAddExerciseRepsInput.getText()).toString(),
                            Objects.requireNonNull(binding.dialogAddExerciseSetsInput.getText()).toString(),
                            Objects.requireNonNull(binding.dialogAddExerciseWeightInput.getText()).toString()));
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), ERROR_INPUT, Toast.LENGTH_SHORT).show();
                }
            });
            binding.dialogAddExerciseButtonCancel.setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }
    }

    private void addExercise(Exercise exercise) {
        exerciseViewModel.addExercise(exercise);
        exerciseViewModel.getExerciseLiveData().observe(this, e -> {
            if (e != null) {
                addExerciseToWorkout(e);
            }
        });
    }

    private void addExerciseToWorkout(Exercise exercise) {
        currentWorkout.getExercises().add(exercise.getId());
        currentWorkout.getExercisesData().add(exercise);
        workoutViewModel.addExercise(currentWorkout.getId(), new HashMap<String, Object>() {{
            put(Constants.DB_EXERCISES, currentWorkout.getExercises());
        }});
    }

    private Exercise createExercise(String name, String rep, String set, String weight) {
        long time = Calendar.getInstance().getTimeInMillis();
        String id = workoutViewModel.generateId();
        History repH = new History(Double.parseDouble(rep), time);
        History setH = new History(Double.parseDouble(set), time);
        History weightH = new History(Double.parseDouble(weight), time);
        return new Exercise(id, name, repH, setH, weightH);
    }

    private boolean validateInput(TextInputEditText input, boolean notZero) {
        boolean textVal = input.getText() != null && !input.getText().toString().trim().isEmpty();
        boolean numberVal = !notZero || input.getText() != null && Double.parseDouble(input.getText().toString()) > 0;
        return textVal && numberVal;
    }

    @SuppressLint("SimpleDateFormat")
    private void initView() {
        Calendar now = Calendar.getInstance();
        binding.workoutDayDate.setText(new SimpleDateFormat("dd.MM.YYYY").format(now.getTime()));
        setCurrentWorkout(now.get(Calendar.DAY_OF_WEEK) - 2);
        binding.workoutDayExercisesList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setCurrentWorkout(int day) {
        int dayValue = day >= 7 ? 0 : day < 0 ? 6 : day;
        currentWorkout = workoutList.get(dayValue);
        binding.workoutDayName.setText(currentWorkout.getDay().getName());
        adapter = new ExerciseAdapter(getContext(), currentWorkout.getExercisesData());
        binding.workoutDayExercisesList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
