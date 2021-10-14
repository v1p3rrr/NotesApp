package com.example.notesapp.Repository.Firebase;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Data.NoteLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FirebaseNoteRepository {

    private static FirebaseNoteRepository instance;
    private DatabaseReference myRef;
    private NoteLiveData notes;

    private FirebaseNoteRepository() {

    }

    public static FirebaseNoteRepository getInstance() {
        if (instance == null)
            instance = new FirebaseNoteRepository();
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

    public void updateNote(List<Note> notes) {
        myRef.setValue(notes);
    }

    public NoteLiveData getAllNotes() {
        return notes;
    }
}
