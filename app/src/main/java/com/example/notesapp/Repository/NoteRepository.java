package com.example.notesapp.Repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NoteRepository {
    private static NoteRepository instance;
    private FirebaseAuth mAuth;

    public static NoteRepository getInstance()
    {
        if (instance == null)
        {
            instance = new NoteRepository();
            return instance;
        }
        return instance;
    }

    public NoteRepository(){
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getMAuth() {
        return mAuth;
    }
}
