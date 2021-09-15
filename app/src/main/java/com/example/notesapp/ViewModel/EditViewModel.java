package com.example.notesapp.ViewModel;

import android.app.Application;


import com.example.notesapp.Repository.NoteRepository;
import com.google.firebase.auth.FirebaseAuth;

public class EditViewModel {
    private final NoteRepository noteRepository;

    public EditViewModel(Application app){
        super(app);
        //TODO: connect to db with notes
    }


}
