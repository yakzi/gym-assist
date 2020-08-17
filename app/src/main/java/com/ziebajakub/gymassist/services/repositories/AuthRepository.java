package com.ziebajakub.gymassist.services.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.view.interfaces.Constants;

import java.util.Objects;

public class AuthRepository {
    private FirebaseAuth firebaseAuth;
    private CollectionReference usersRef;

    public AuthRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseFirestore.getInstance().collection(Constants.USERS);
    }

    public MutableLiveData<User> signInWithGoogle(AuthCredential googleAuthCredential) {
        MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                boolean isNewUser = Objects.requireNonNull(Objects.requireNonNull(authTask.getResult()).getAdditionalUserInfo()).isNewUser();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    String name = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                    User user = new User(uid, name, email);
                    user.setNew(isNewUser);
                    authenticatedUserMutableLiveData.setValue(user);
                }
            } else {
                authenticatedUserMutableLiveData.setValue(null);
            }
        });
        return authenticatedUserMutableLiveData;
    }

    public MutableLiveData<User> addUserToDatabase(User user) {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        DocumentReference userRef = usersRef.document(user.getUid());
        userRef.set(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userMutableLiveData.setValue(user);
            } else {
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    public MutableLiveData<User> getUserFromDatabase(String id) {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        DocumentReference userRef = usersRef.document(id);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot!= null && documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    userMutableLiveData.setValue(user);
                } else {
                    userMutableLiveData.setValue(null);
                }
            } else {
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    public boolean isLoggedUser() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public String getLoggedUserId() {
        return firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;
    }
}
