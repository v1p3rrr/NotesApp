package com.example.notesapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Repository.Firebase.FirebaseNoteRepository;
import com.example.notesapp.Repository.Network.WordsImplementation;
import com.example.notesapp.Repository.Room.NoteRoomRepository;

import java.util.ArrayList;
import java.util.List;

public class EditViewModel extends AndroidViewModel {

    //private final FirebaseNoteRepository noteRepository;
    private NoteRoomRepository noteRepository;
    //private ArrayList<Note> displayList;


    public EditViewModel(Application app) {
        super(app);
        //noteRepository = FirebaseNoteRepository.getInstance();
        noteRepository = NoteRoomRepository.getInstance();
        //displayList = new ArrayList<>();
    }

   public void init() throws IllegalAccessException {
//        noteRepository.init();
       noteRepository = new NoteRoomRepository(getApplication());
   }

    public LiveData<List<Note>> getNotes() {
        return noteRepository.getAllNotes();
    }


//    public void setDisplayList(List<Note> notes){
//        this.displayList = (ArrayList<Note>) notes;
//    }

    public void saveNewNote(Note note){
        noteRepository.addNote(note);
        //displayList.add(note);
        //noteRepository.saveNote(displayList);
    }

    public void saveEditedNote(Note note, int id){
        noteRepository.updateNote(note);
        //displayList.set(id, note);
        //noteRepository.saveNote(displayList);
    }

    public LiveData<List<String>> getSynonymsList(String word) {
        return WordsImplementation.getInstance().getSynonyms(word);
    }

    public Note getNoteById (int id){
        return noteRepository.getNoteById(id);
    }

    public Note getLastNote(){
        return noteRepository.getLastNote();
    }
}
