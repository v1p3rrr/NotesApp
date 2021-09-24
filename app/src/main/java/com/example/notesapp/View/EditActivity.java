package com.example.notesapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.notesapp.ViewModel.EditViewModel;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    private EditText editTitle, editTextNote;
    private int noteId;
    private EditViewModel editViewModel; //todo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color=\"black\">" + getString(R
                .string.app_name) + "</font>")); // Перекраска заголовка Actionbar
        getIntentMain();
        init();
    }

    public void init() {
        editTitle = findViewById(R.id.editTitle);
        editTextNote = findViewById(R.id.editTextNote);
        this.editViewModel = new ViewModelProvider(this).get(EditViewModel.class);
        try {
            editViewModel.init();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        editViewModel.getNotes().observe(this, notes -> {
            if (notes != null){
                editViewModel.setDisplayList(notes);
                if (noteId != -1) {
                    editTitle.setText(notes.get(noteId).getTitle());
                    editTextNote.setText(notes.get(noteId).getTextNote());
                }
            }
        });
    }


    public void onClickSave() { // Сохранение заметки
        String title = editTitle.getText().toString();
        String textNote = editTextNote.getText().toString();
        Note note = new Note(title, textNote);
        if (noteId != -1) saveEdited(note);
        else saveNew(note);
    }

    private void getIntentMain() {
        Intent i = getIntent();
        if (i != null) {
            noteId = i.getIntExtra("noteId", -1);
        }
    }


    private void saveNew(Note note) { // Сохранение новой заметки
        if (!TextUtils.isEmpty(note.getTitle().trim())) {
            editViewModel.saveNewNote(note);
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
        if (!TextUtils.isEmpty(note.getTitle().trim())) {
            editViewModel.saveEditedNote(note, noteId);
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