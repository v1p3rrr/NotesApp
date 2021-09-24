package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Repository.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class EditViewModel extends AndroidViewModel {

    private final NoteRepository noteRepository;
    private ArrayList<Note> displayList;


    public EditViewModel(Application app) {
        super(app);
        noteRepository = NoteRepository.getInstance();
        displayList = new ArrayList<>();
    }

    public void init() throws IllegalAccessException {
        noteRepository.init();
    }

    public LiveData<List<Note>> getNotes() {
        return noteRepository.getNotes();
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

}
