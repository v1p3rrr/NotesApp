package com.example.notesapp.Repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.notesapp.Data.Note;
import com.example.notesapp.View.MainAdapter;
import com.example.notesapp.View.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NoteRepository extends LiveData {

    private Application app;
    private String NOTE_KEY = "Note";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference noteDataBase = database.getReference(NOTE_KEY);
    private List<Note> listData;
    private List<String> listId;
    private MainAdapter mainAdapter;

    public NoteRepository(Note value) {
        super(value);
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }


    private void readFromDb() { // Чтение заметок из БД и обновление списка
        noteDataBase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listData.size() > 0) listData.clear();
                if (listId.size() > 0) listId.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Note note = ds.getValue(Note.class);
                    String id = ds.getKey();
                    assert note != null;
                    listId.add(id);
                    listData.add(note);
                }
                mainAdapter.updateAdapter(listData, listId);
                mainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Ошибка чтения из базы данных", Toast.LENGTH_SHORT).show();
            }
        };
        //noteDataBase.addValueEventListener(vListener);
    }
}
