package com.example.notesapp.Data;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NoteLiveData extends LiveData<List<Note>> {

    private final DatabaseReference databaseReference;

    public final ValueEventListener valueEventListener = new ValueEventListener(){
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Note> notes = new ArrayList<>();
            for (DataSnapshot child : snapshot.getChildren()) {
                notes.add(child.getValue(Note.class));
            }
            setValue(notes);
        }

        @Override
        public void onCancelled (@NonNull DatabaseError databaseError){

        }
    };

    public NoteLiveData(DatabaseReference ref) {
        this.databaseReference = ref;
    }

    @Override
    protected void onActive() {
        super.onActive();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(valueEventListener);
    }
}
