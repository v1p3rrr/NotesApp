package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.notesapp.Repository.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;

    public AuthViewModel(Application app){
        super(app);
        authRepository = AuthRepository.getInstance();
    }

    public FirebaseAuth getMAuth(){
        return authRepository.getMAuth();
    }
}
