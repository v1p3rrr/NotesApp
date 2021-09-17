package com.example.notesapp.Repository;

import com.google.firebase.auth.FirebaseAuth;

public class AuthRepository {
    private static AuthRepository instance;
    private final FirebaseAuth mAuth;

    public static AuthRepository getInstance()
    {
        if (instance == null)
        {
            instance = new AuthRepository();
            return instance;
        }
        return instance;
    }

    private AuthRepository(){
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getMAuth() {
        return mAuth;
    }
}
