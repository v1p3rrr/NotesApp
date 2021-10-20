package com.example.notesapp.View;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.Data.Note;
import com.example.notesapp.R;
import com.example.notesapp.ViewModel.ImageViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.util.Objects;

public class ImageActivity extends AppCompatActivity {

    private ImageViewModel imageViewModel;
    private ImageView imageView;
    private FloatingActionButton addButton;
    private Uri uri;
    private int noteId;
    private TextView activityDescription;
    private String noteText, noteTitle;
    private boolean isChanged;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color=\"black\">" + getString(R
                .string.app_name) + "</font>"));
        getIntentEdit();
        init();
    }


    public void init() {
        imageView = findViewById(R.id.imageViewNote);
        addButton = findViewById(R.id.addImageButton);
        changeImage();
        isChanged = false;

        activityDescription = findViewById(R.id.textViewTest);
        activityDescription.setText("Прикреплённое изображение");

        this.imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        imageViewModel.init();

        imageViewModel.getNotes().observe(this, notes -> {
            if (notes != null){
                if (noteId != -1 && imageViewModel.getNoteById(noteId).getImage() != null) {
                    imageView.setImageURI(Uri.parse(imageViewModel.getNoteById(noteId).getImage()));
                    Log.i(TAG, "GET IMG" + imageViewModel.getNoteById(noteId).getImage());
                } else Log.i(TAG, "imageActivity error: noteId was not received (has default value of -1)");
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (noteId == -1 && isChanged){
            Intent i = new Intent(this, EditActivity.class);
            i.putExtra("noteId", imageViewModel.getLastNote().getNoteId());
            startActivity(i);
            finishAffinity();
            finish();
        }
    }

    public void getIntentEdit(){
        Intent i = getIntent();
        if (i != null) {
            noteId = i.getIntExtra("noteId", -1);
            noteTitle = i.getStringExtra("noteTitle");
            noteText = i.getStringExtra("noteText");
        }
    }


    public void changeImage(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"pickImage"),1);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==1){
            uri=data.getData();
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageView.setImageURI(uri);
            if (noteId!=-1) {
                Note note = imageViewModel.getNoteById(noteId);
                note.setTextNote(noteText);
                note.setTitle(noteTitle);
                note.setImage(uri.toString());
                imageViewModel.saveEditedNote(note, noteId);
                Log.i(TAG, "old Image saved");
            } else {
                Note note = new Note(noteTitle, noteText);
                note.setImage(uri.toString());
                imageViewModel.saveNewNote(note);
            }
            isChanged = true;
            Toast.makeText(this, "Изображение сохранено", Toast.LENGTH_SHORT).show();

            Log.i(TAG, "Image saved");
            Log.i(TAG, "SET IMG" + uri.toString());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}
