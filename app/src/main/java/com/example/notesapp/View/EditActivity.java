package com.example.notesapp.View;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
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
    private EditViewModel editViewModel;

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
                if (noteId != -1) {
                    editTitle.setText(editViewModel.getNoteById(noteId).getTitle());
                    editTextNote.setText(editViewModel.getNoteById(noteId).getTextNote());
                } else Log.i(TAG, "CmonBruh");
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (intent != null) {
                noteId = intent.getIntExtra("noteId", -1);
            }
        }
    }

    public void onClickSave() { // Сохранение заметки
        if (noteId != -1) saveEdited();
        else saveNew();
    }

    private void getIntentMain() {
        Intent i = getIntent();
        Log.i(TAG, String.valueOf(i.hashCode()) + "Edit");
        if (i != null) {
            noteId = i.getIntExtra("noteId", -1);
        }
    }


    private void saveNew() { // Сохранение новой заметки
        if (!TextUtils.isEmpty(editTitle.getText().toString().trim())) {
            editViewModel.saveNewNote(new Note(editTitle.getText().toString(), editTextNote.getText().toString()));
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finishAffinity();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Введите заголовок", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEdited() { // Сохранение существующей заметки
        if (!TextUtils.isEmpty(editTitle.getText().toString().trim())) {
            Note note = editViewModel.getNoteById(noteId);
            note.setTextNote(editTextNote.getText().toString());
            note.setTitle(editTitle.getText().toString());
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