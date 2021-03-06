package com.example.notesapp.View;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.Data.Note;
import com.example.notesapp.R;
import com.example.notesapp.ViewModel.EditViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    private AutoCompleteTextView editTitle;
    private EditText editTextNote;
    private FloatingActionButton showSynonymsButton;
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
        editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });


        editTextNote = findViewById(R.id.editTextNote);
        editTextNote.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    editTextNote.setCursorVisible(false);
                    editTextNote.clearFocus();
                }
            }
        });

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
                    Log.i(TAG, "GET IMG EDIT " + editViewModel.getNoteById(noteId).getImage());
                } else Log.i(TAG, "editActivity error: noteId was not received (has default value of -1)");
            }
        });

        showSynonymsButton = findViewById(R.id.synonymCheck);
        showSynonymsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                synonymShow();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onClickSave();
    }

    public void onClickMoveToImage(){
        if (!TextUtils.isEmpty(editTitle.getText().toString().trim())) {
            if (noteId == -1) {
                editViewModel.saveNewNote(new Note(editTitle.getText().toString(), editTextNote.getText().toString()));
            } else saveEdited();
            Intent i = new Intent(this, ImageActivity.class);
            i.putExtra("noteId", editViewModel.getLastNote().getNoteId());
            i.putExtra("noteTitle", editTitle.getText().toString());
            i.putExtra("noteText", editTextNote.getText().toString());
            startActivity(i);

        }
        else {
            Toast.makeText(getApplicationContext(), "Введите заголовок", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSave() { // Сохранение заметки
        if (noteId != -1) saveEdited();
        else saveNew();
    }

    private void getIntentMain() {
        Intent i = getIntent();
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

    private void synonymShow() {
        editTitle = findViewById(R.id.editTitle);
        if (editTitle.getText().toString().trim().matches("^[a-zA-Z]+$")) {
            editViewModel.getSynonymsList(editTitle.getText().toString().trim()).observe(this, (List<String> values) -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        values
                );
                editTitle.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                editTitle.showDropDown();
            });
        } else System.out.println("wrong pattern");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addNote) {
            onClickSave();
        } else if (item.getItemId() == R.id.addImageButton) {
            onClickMoveToImage();
        }
        return true;
    }

}