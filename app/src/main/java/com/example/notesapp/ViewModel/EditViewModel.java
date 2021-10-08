package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Repository.Firebase.FirebaseNoteRepository;

import java.util.ArrayList;
import java.util.List;

public class EditViewModel extends AndroidViewModel {

    private final FirebaseNoteRepository firebaseNoteRepository;
    private ArrayList<Note> displayList;


    public EditViewModel(Application app) {
        super(app);
        firebaseNoteRepository = FirebaseNoteRepository.getInstance();
        displayList = new ArrayList<>();
    }

    public void init() throws IllegalAccessException {
        firebaseNoteRepository.init();
    }

    public LiveData<List<Note>> getNotes() {
        return firebaseNoteRepository.getNotes();
    }


    public void setDisplayList(List<Note> notes){
        this.displayList = (ArrayList<Note>) notes;
    }

    public void saveNewNote(Note note){
        displayList.add(note);
        firebaseNoteRepository.saveNote(displayList);
    }

    public void saveEditedNote(Note note, int id){
        displayList.set(id, note);
        firebaseNoteRepository.saveNote(displayList);
    }

}
