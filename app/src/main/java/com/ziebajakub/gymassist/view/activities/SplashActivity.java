package com.ziebajakub.gymassist.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ziebajakub.gymassist.R;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.view.interfaces.Constants;
import com.ziebajakub.gymassist.viewmodels.AuthViewModel;

public class SplashActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initAuthViewModel();
        checkUser();
    }

    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void changeToActivity(Activity activity, @Nullable User user) {
        Intent intent = new Intent(this, activity.getClass());
        if (user != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.USER, user);
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    private void checkUser() {
        if(authViewModel.isLoggedUser()) {
            getUserFromDatabase();
        } else {
            changeToActivity(new LoginActivity(), null);
        }
    }


    private void getUserFromDatabase() {
        authViewModel.getUserFromDatabase(authViewModel.getLoggedUserId());
        authViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                changeToActivity(new MainActivity(), user);
            } else {
                changeToActivity(new LoginActivity(), user);
            }
        });
    }

}
