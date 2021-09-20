package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.notesapp.Repository.NoteRepository;

public class EditViewModel extends AndroidViewModel {
    private final NoteRepository noteRepository;

    public EditViewModel(Application app){
        super(app);
        noteRepository = NoteRepository.getInstance();
        //TODO: connect to db with notes
    }


}
