package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.notesapp.Repository.Firebase.FirebaseAuthRepository;
import com.google.firebase.auth.FirebaseAuth;

public class AuthViewModel extends AndroidViewModel {
    private final FirebaseAuthRepository firebaseAuthRepository;

    public AuthViewModel(Application app){
        super(app);
        firebaseAuthRepository = FirebaseAuthRepository.getInstance();
    }

    public FirebaseAuth getMAuth(){
        return firebaseAuthRepository.getMAuth();
    }
}
