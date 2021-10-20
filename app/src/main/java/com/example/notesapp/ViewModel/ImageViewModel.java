package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Repository.Room.NoteRoomRepository;

import java.util.List;

public class ImageViewModel extends AndroidViewModel {

    private NoteRoomRepository noteRepository;


    public ImageViewModel(Application app) {
        super(app);
        noteRepository = NoteRoomRepository.getInstance();
    }

    public void init(){
        noteRepository = new NoteRoomRepository(getApplication());
    }

    public LiveData<List<Note>> getNotes() {
        return noteRepository.getAllNotes();
    }


    public void saveNewNote(Note note){
        noteRepository.addNote(note);
    }

    public void saveEditedNote(Note note){
        noteRepository.updateNote(note);
    }

    public Note getNoteById (int id){
        return noteRepository.getNoteById(id);
    }

}
