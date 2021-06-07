package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditActivity extends AppCompatActivity {

    private EditText editTitle, editTextNote;
    private DatabaseReference noteDataBase;
    private String NOTE_KEY = "Note";
    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        getIntentMain();
        if (noteId != null) {
            setEditNote();
        }
    }

    public void init() {
        editTitle = findViewById(R.id.editTitle);
        editTextNote = findViewById(R.id.editTextNote);
        noteDataBase = FirebaseDatabase.getInstance().getReference(NOTE_KEY);
    }


    public void onClickSave(View view) {
        System.out.println("button pressed1");
        String id = "ID";
        String title = editTitle.getText().toString();
        String textNote = editTextNote.getText().toString();
        Note note = new Note(id, title, textNote);
        if (noteId != null) saveEdited(note);
        else saveNew(note);
    }

    private void getIntentMain() {
        Intent i = getIntent();
        if (i != null) {
            noteId = i.getStringExtra("noteId");
        }
    }

    private void setEditNote() {
        noteDataBase.child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Note editNote = snapshot.getValue(Note.class);
                assert editNote != null;
                editTitle.setText(editNote.title);
                editTextNote.setText(editNote.textNote);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void saveNew(Note note) {
        if (!TextUtils.isEmpty(note.title.trim())) {
            noteDataBase.push().setValue(note);
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "Введите заголовок", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEdited(Note note) {
        if (!TextUtils.isEmpty(note.title.trim())) {
            noteDataBase.child(noteId).setValue(note);
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "Введите заголовок", Toast.LENGTH_SHORT).show();
        }
    }

}