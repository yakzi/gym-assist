package com.ziebajakub.gymassist;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.AuthCredential;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private LiveData<User> userLiveData;

    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    LiveData<User> getUserLiveData(){
        return userLiveData;
    }

    void signInWithGoogle(AuthCredential googleAuthCredential) {
        userLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential);
    }

    boolean isLoggedUser(){
        return authRepository.isLoggedUser();
    }

    void logout(){
        authRepository.logout();
        userLiveData = null;
    }
}
