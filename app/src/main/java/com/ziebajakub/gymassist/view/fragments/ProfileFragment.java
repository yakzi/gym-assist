package com.ziebajakub.gymassist.view.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.ziebajakub.gymassist.R;
import com.ziebajakub.gymassist.databinding.FragmentProfileBinding;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.view.interfaces.Constants;
import com.ziebajakub.gymassist.viewmodels.AuthViewModel;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private FragmentProfileBinding binding;
    private AuthViewModel authViewModel;
    private User user;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        initAuthViewModel();
        setListeners();
        initProfileInfo();

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void initProfileInfo() {
        binding.profileName.setText(user.getName());
        String lastWeight = user.getWeights().size() > 0
                ? String.valueOf(user.getWeights().get(user.getWeights().size() - 1).getValue())
                : "Not set";
        binding.profileWeightValue.setText(lastWeight);
        if (getContext() != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            Uri profileImage = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
            Glide.with(getContext())
                    .load(profileImage)
                    .centerCrop()
                    .placeholder(R.drawable.ic_account)
                    .into(binding.profileImage);
        }
    }

    private void initAuthViewModel() {
        if (getActivity() != null) {
            authViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);
            authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    initProfileInfo();
                }
            });
        }
    }

    private void setListeners() {
        binding.profileLogoutButton.setOnClickListener(this);
        binding.profileWeightButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_logout_button:
                authViewModel.logout();
                break;
            case R.id.profile_weight_button:
                getNavigation().changeFragment(this, WeightHistoryFragment.newInstance(user), true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
