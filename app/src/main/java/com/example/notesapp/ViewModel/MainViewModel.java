package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Repository.AuthRepository;
import com.example.notesapp.Repository.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel { // Когда VM, когда Android VM?
    private final NoteRepository noteRepository;
    private final AuthRepository authRepository;
    private ArrayList<Note> displayList;


    public MainViewModel(Application app) {
        super(app);
        noteRepository = NoteRepository.getInstance();
        authRepository = AuthRepository.getInstance();
        displayList = new ArrayList<>();
    }

    public void init() throws IllegalAccessException {
        noteRepository.init();
    }

    public LiveData<List<Note>> getNotes() {
        return noteRepository.getNotes();
    }

    public void deleteNote(int id){
        displayList.remove(id);
        noteRepository.updateNotes(displayList);
    }

    public void setDisplayList(List<Note> notes){
        this.displayList = (ArrayList<Note>) notes;
    }

    public void saveNewNote(Note note){
        displayList.add(note);
        noteRepository.saveNote(displayList);
    }

    public void saveEditedNote(Note note, int id){
        displayList.set(id, note);
        noteRepository.saveNote(displayList);
    }

    public void logout(){
        authRepository.getMAuth().signOut();
    }

    // SharedPreference ce wo?
}
