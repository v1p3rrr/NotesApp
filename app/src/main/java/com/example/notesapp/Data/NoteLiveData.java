package com.example.notesapp.Data;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.notesapp.View.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NoteLiveData extends LiveData<List<Note>> {
    public final ValueEventListener valueEventListener = new ValueEventListener(){
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            try {
                List<Note> notes = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    notes.add(child.getValue(Note.class));
                }
               setValue(notes);
            }
            catch (Exception e){
                Log.i(TAG, "onDataChange: abobus");
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    };

    DatabaseReference databaseReference;

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
