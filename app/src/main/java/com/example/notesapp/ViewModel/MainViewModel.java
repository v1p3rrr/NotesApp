package com.example.notesapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Repository.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final NoteRepository noteRepository;
    private ArrayList<Note> displayList;


    public MainViewModel() {
        noteRepository = NoteRepository.getInstance();
        displayList = new ArrayList<>();
    }

    public LiveData<List<Note>> getNotes() {
        return noteRepository.getNotes();
    }
}
