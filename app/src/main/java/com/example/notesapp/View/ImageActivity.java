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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


        this.imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        imageViewModel.init();

        ContentResolver cr = getContentResolver();


        imageViewModel.getNotes().observe(this, notes -> {
            if (notes != null){
                if (noteId != -1 && imageViewModel.getNoteById(noteId).getImage() != null) {
                    try {
                        getApplicationContext().getContentResolver().openFileDescriptor(
                                Uri.parse(imageViewModel.getNoteById(noteId).getImage()), "r").getFileDescriptor();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //uri = Uri.parse(imageViewModel.getNoteById(noteId).getImage());
                    imageView.setImageURI(uri);
                } else Log.i(TAG, "imageActivity error: noteId was not received (has default value of -1)");
            }
        });

    }



    public void getIntentEdit(){
        Intent i = getIntent();
        if (i != null) {
            noteId = i.getIntExtra("noteId", -1);
        }
    }

    public void changeImage(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"pickImage"),1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onClickSave();
    }

    public void onClickSave() { // Сохранение заметки
        if (uri != null) {
            Note note = imageViewModel.getNoteById(noteId);
            note.setImage(uri.toString());
            imageViewModel.saveEditedNote(note);
            Toast.makeText(this, "Изображение сохранено", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("noteId", noteId);
            startActivity(i);
            finishAffinity();
            finish();
        } else {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("noteId", noteId);
            startActivity(i);
            finishAffinity();
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==1){
            uri=data.getData();
            imageView.setImageURI(data.getData());
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //imageViewModel.saveEditedNote(,uri.toString());
        }
    }
}
