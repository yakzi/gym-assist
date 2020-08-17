package com.ziebajakub.gymassist.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ziebajakub.gymassist.R;
import com.ziebajakub.gymassist.databinding.ActivityLoginBinding;
import com.ziebajakub.gymassist.services.models.User;
import com.ziebajakub.gymassist.view.interfaces.Constants;
import com.ziebajakub.gymassist.viewmodels.AuthViewModel;

import static com.ziebajakub.gymassist.view.interfaces.Constants.RC_SIGN_IN;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private GoogleSignInClient googleSignInClient;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAuthViewModel();
        initSignInButton();
        initGoogleSignInClient();
    }

    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void initSignInButton() {
        binding.googleLoginButton.setOnClickListener(v -> signIn());
    }

    private void initGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void getGoogleAuthCredential(GoogleSignInAccount googleSignInAccount) {
        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInWithGoogleAuthCredential(googleAuthCredential);
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential) {
        authViewModel.signInWithGoogle(googleAuthCredential);
        authViewModel.getUserLiveData().observe(this, authenticatedUser -> {
            if(authenticatedUser != null){
                if (authenticatedUser.isNew()) {
                    addUserToDatabase(authenticatedUser);
                } else {
                    getUserFromDatabase(authenticatedUser.getUid());
                }
            }
        });
    }

    private void addUserToDatabase(User userToAdd) {
        authViewModel.addUserToDatabase(userToAdd);
        authViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                changeToMainView(user);
            } else {
                Toast.makeText(this, "Error while uploading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserFromDatabase(String id) {
        authViewModel.getUserFromDatabase(id);
        authViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                changeToMainView(user);
            } else {
                Toast.makeText(this, "Error while fetching data from database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeToMainView(User user) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.USER, user);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    getGoogleAuthCredential(googleSignInAccount);
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Error while login into Google", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
