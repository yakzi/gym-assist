package com.ziebajakub.gymassist.view.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ziebajakub.gymassist.view.interfaces.NavigationListener;

public class BaseFragment extends Fragment {

    private NavigationListener navigationListener;

    NavigationListener getNavigation() {
        return navigationListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationListener = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NavigationListener) {
            navigationListener = (NavigationListener) context;
        }
    }
}