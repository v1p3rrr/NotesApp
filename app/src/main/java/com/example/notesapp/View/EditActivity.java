package com.example.notesapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notesapp.Data.Note;
import com.example.notesapp.R;
import com.example.notesapp.View.MainActivity;
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
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R
                .string.app_name) + "</font>")); // Перекраска заголовка Actionbar
        init();
        getIntentMain();
        if (noteId != null) {
            setEditNote(); // Проверка новая заметка или изменение старой
        }
    }

    public void init() {
        editTitle = findViewById(R.id.editTitle);
        editTextNote = findViewById(R.id.editTextNote);
        noteDataBase = FirebaseDatabase.getInstance().getReference(NOTE_KEY);
    }


    public void onClickSave() { // Сохранение заметки
        String title = editTitle.getText().toString();
        String textNote = editTextNote.getText().toString();
        Note note = new Note(title, textNote);
        if (noteId != null) saveEdited(note);
        else saveNew(note);
    }

    private void getIntentMain() {
        Intent i = getIntent();
        if (i != null) {
            noteId = i.getStringExtra("noteId");
        }
    }

    private void setEditNote() { // Открытие существующей заметки
        noteDataBase.child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override // Получение списка заметок из бд и подстановка заголовка и текста по полученному ID
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

    private void saveNew(Note note) { // Сохранение новой заметки
        if (!TextUtils.isEmpty(note.title.trim())) {
            noteDataBase.push().setValue(note);
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finishAffinity();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Введите заголовок", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEdited(Note note) { // Сохранение существующей заметки
        if (!TextUtils.isEmpty(note.title.trim())) {
            noteDataBase.child(noteId).setValue(note);
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Введите заголовок", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onClickSave();
        return true;
    }

}