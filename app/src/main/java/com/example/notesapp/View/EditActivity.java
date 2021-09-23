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
import com.example.notesapp.ViewModel.MainViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditActivity extends AppCompatActivity {

    private EditText editTitle, editTextNote;
    private String NOTE_KEY = "Note";
    private int noteId;
    private MainViewModel mainViewModel; //todo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R
                .string.app_name) + "</font>")); // Перекраска заголовка Actionbar
        init();
        getIntentMain();
        if (noteId != 0) {
            setEditNote(); // Проверка новая заметка или изменение старой
        }
    }

    public void init() {
        editTitle = findViewById(R.id.editTitle);
        editTextNote = findViewById(R.id.editTextNote);

        mainViewModel.getNotes().observe(this, notes -> {
            if (notes != null){
                mainViewModel.setDisplayList(notes);
            }
        });
    }


    public void onClickSave() { // Сохранение заметки
        String title = editTitle.getText().toString();
        String textNote = editTextNote.getText().toString();
        Note note = new Note(title, textNote);
        if (noteId != 0) saveEdited(note);
        else saveNew(note);
    }

    private void getIntentMain() {
        Intent i = getIntent();
        if (i != null) {
            noteId = i.getIntExtra("noteId", 0);
        }
    }

    private void setEditNote() { // Открытие существующей заметки
        editTitle.setText(mainViewModel.getNotes().getValue().get(noteId).getTitle());
        editTextNote.setText(mainViewModel.getNotes().getValue().get(noteId).getTitle());
    }

    private void saveNew(Note note) { // Сохранение новой заметки
        if (!TextUtils.isEmpty(note.getTitle().trim())) {
            mainViewModel.saveNewNote(note);
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
            mainViewModel.saveEditedNote(note, noteId);
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