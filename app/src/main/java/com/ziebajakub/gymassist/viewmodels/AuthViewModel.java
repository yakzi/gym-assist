package com.ziebajakub.gymassist.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.services.repositories.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<User> userLiveData;

    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
        userLiveData = new MutableLiveData<>();
    }

    public LiveData<User> getUserLiveData(){
        return userLiveData;
    }

    public void addUserToDatabase(User user) {
        userLiveData = authRepository.addUserToDatabase(user);
    }

    public void getUserFromDatabase(String id) {
        userLiveData = authRepository.getUserFromDatabase(id);
    }

    public void signInWithGoogle(AuthCredential googleAuthCredential) {
        userLiveData = authRepository.signInWithGoogle(googleAuthCredential);
    }

    public boolean isLoggedUser(){
        return authRepository.isLoggedUser();
    }

    public String getLoggedUserId() {
        return authRepository.getLoggedUserId();
    }

    public void logout(){
        userLiveData.setValue(null);
        authRepository.logout();
    }
}
