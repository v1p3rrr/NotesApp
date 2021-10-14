package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Repository.Firebase.FirebaseAuthRepository;
import com.example.notesapp.Repository.Firebase.FirebaseNoteRepository;
import com.example.notesapp.Repository.Room.NoteRoomRepository;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    //private final FirebaseNoteRepository noteRepository;
    private final FirebaseAuthRepository firebaseAuthRepository;
    private NoteRoomRepository noteRepository;
//    private ArrayList<Note> displayList;


    public MainViewModel(Application app) {
        super(app);
        //noteRepository = FirebaseNoteRepository.getInstance();
        noteRepository = NoteRoomRepository.getInstance();
        firebaseAuthRepository = FirebaseAuthRepository.getInstance();
//        displayList = new ArrayList<>();
    }

    public void init(Application app) throws IllegalAccessException {
//        noteRepository.init();
        noteRepository = new NoteRoomRepository(app);
   }

    public LiveData<List<Note>> getNotes() {
        return noteRepository.getAllNotes();
    }

    public void deleteNote(/*int id*/Note note){
        noteRepository.deleteNote(note);
        //displayList.remove(id);
        //noteRepository.updateNotes(displayList);
    }
//
//    public void setDisplayList(List<Note> notes){
//        this.displayList = (ArrayList<Note>) notes;
//    }


    public void logout(){
        firebaseAuthRepository.getMAuth().signOut();
    }


}
