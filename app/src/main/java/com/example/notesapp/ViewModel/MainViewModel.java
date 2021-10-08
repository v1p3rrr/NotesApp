package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Repository.Firebase.FirebaseAuthRepository;
import com.example.notesapp.Repository.Firebase.FirebaseNoteRepository;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel { // Когда VM, когда Android VM?
    private final FirebaseNoteRepository firebaseNoteRepository;
    private final FirebaseAuthRepository firebaseAuthRepository;
    private ArrayList<Note> displayList;


    public MainViewModel(Application app) {
        super(app);
        firebaseNoteRepository = FirebaseNoteRepository.getInstance();
        firebaseAuthRepository = FirebaseAuthRepository.getInstance();
        displayList = new ArrayList<>();
    }

    public void init() throws IllegalAccessException {
        firebaseNoteRepository.init();
    }

    public LiveData<List<Note>> getNotes() {
        return firebaseNoteRepository.getNotes();
    }

    public void deleteNote(int id){
        displayList.remove(id);
        firebaseNoteRepository.updateNotes(displayList);
    }

    public void setDisplayList(List<Note> notes){
        this.displayList = (ArrayList<Note>) notes;
    }


    public void logout(){
        firebaseAuthRepository.getMAuth().signOut();
    }


}
