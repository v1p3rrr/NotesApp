package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.notesapp.Repository.NoteRepository;
import com.google.firebase.auth.FirebaseAuth;

public class NoteViewModel extends AndroidViewModel {
    private final NoteRepository noteRepository;

    public NoteViewModel(Application app){
        super(app);
        noteRepository = NoteRepository.getInstance();
    }

    public FirebaseAuth getMAuth(){
        return noteRepository.getMAuth();
    }
}
