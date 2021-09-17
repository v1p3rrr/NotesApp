package com.example.notesapp.Repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Data.NoteLiveData;
import com.example.notesapp.View.MainAdapter;
import com.example.notesapp.View.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoteRepository {

    private static NoteRepository instance;
    private DatabaseReference myRef;
    private NoteLiveData notes;

    private NoteRepository() {

    }

    public static synchronized NoteRepository getInstance() {
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
            throw new IllegalAccessException("5051");
    }

    public void saveNote(List<Note> notes) {
        myRef.setValue(notes);
    }

    public void removeNotes() {
        myRef.setValue(new ArrayList<Note>());
    }

    public NoteLiveData getNotes() {
        return notes;
    }
}
