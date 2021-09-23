package com.example.notesapp.Repository;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Data.NoteLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NoteRepository {

    private static NoteRepository instance;
    private DatabaseReference myRef;
    private NoteLiveData notes;

    private NoteRepository() {

    }

    public static NoteRepository getInstance() {
        if (instance == null)
            instance = new NoteRepository();
        return instance;
    }

    public void init() throws IllegalAccessException {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UID = user.getUid();

        if (UID != null) {
            myRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Notes");
            notes = new NoteLiveData(myRef);
        } else
            throw new IllegalAccessException("initialization exception");
    }

    public void saveNote(List<Note> notes) {
        myRef.setValue(notes);
    }

    public void updateNotes(List<Note> notes) {
        myRef.setValue(notes);
    }

    public NoteLiveData getNotes() {
        return notes;
    }
}
