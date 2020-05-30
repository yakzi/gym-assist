package com.ziebajakub.gymassist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziebajakub.gymassist.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment implements OnClickListener{

    private FragmentProfileBinding binding;
    private AuthViewModel authViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        initAuthViewModel();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.logout.setOnClickListener(this::onClick);
    }


    private void logout() {
        authViewModel.logout();
        ((MainActivity) getActivity()).changeToLoginView();
    }

    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logout:
                logout();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
