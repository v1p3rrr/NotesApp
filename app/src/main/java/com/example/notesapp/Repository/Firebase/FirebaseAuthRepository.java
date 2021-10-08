package com.example.notesapp.Repository.Firebase;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthRepository {
    private static FirebaseAuthRepository instance;
    private final FirebaseAuth mAuth;

    public static FirebaseAuthRepository getInstance()
    {
        if (instance == null)
        {
            instance = new FirebaseAuthRepository();
            return instance;
        }
        return instance;
    }

    private FirebaseAuthRepository(){
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getMAuth() {
        return mAuth;
    }
}
