package com.example.notesapp.View;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.Data.Note;
import com.example.notesapp.R;
import com.example.notesapp.ViewModel.ImageViewModel;
import com.giphy.sdk.core.GPHCore;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.RenditionType;
import com.giphy.sdk.ui.GPHContentType;
import com.giphy.sdk.ui.GPHSettings;
import com.giphy.sdk.ui.Giphy;
import com.giphy.sdk.ui.themes.GPHTheme;
import com.giphy.sdk.ui.themes.GridType;
import com.giphy.sdk.ui.views.GiphyDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.Objects;

import timber.log.Timber;

public class ImageActivity extends AppCompatActivity {

    private ImageViewModel imageViewModel;
    private ImageView imageView;
    private FloatingActionButton addButton;
    private FrameLayout gifButton;
    private int noteId;
    private String noteText, noteTitle;
    private boolean isChanged;

    private final static String YOUR_API_KEY = "ndkvIvZanq5ktA0eEalC9GUMJ8vbuUug";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Giphy.INSTANCE.configure(ImageActivity.this, YOUR_API_KEY, false);

        setContentView(R.layout.activity_image);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color=\"black\">" + getString(R
                .string.app_name) + "</font>"));
        getIntentEdit();
        init();
    }


    public void init() {
        imageView = findViewById(R.id.imageViewNote);
        addButton = findViewById(R.id.addImageButton);
        gifButton = findViewById(R.id.launchGiphyBtn);

        changeImage();
        changeGif();
        removeImage();
        isChanged = false;

        TextView activityDescription = findViewById(R.id.textViewTest);
        activityDescription.setText("Прикреплённое изображение");

        this.imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        imageViewModel.init();
        try {
            imageViewModel.getNotes().observe(this, notes -> {
                if (notes != null) {
                    if (noteId != -1 && imageViewModel.getNoteById(noteId).getImage() != null) {
                        Glide.with(ImageActivity.this).load(imageViewModel.getNoteById(noteId).getImage()).into(imageView);
                        //imageView.setImageURI(Uri.parse(imageViewModel.getNoteById(noteId).getImage()));
                        Timber.i("GET IMG%s", imageViewModel.getNoteById(noteId).getImage());
                    } else
                        Timber.i("imageActivity error: noteId was not received (has default value of -1)");
                }
            });
        } catch (java.lang.SecurityException e) {
            e.printStackTrace();
            System.out.println("exception caught");
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (noteId != -1 && isChanged){
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
        addButton.setOnClickListener(v -> getActivityResultRegistry().register("key", new ActivityResultContracts.OpenDocument() , result -> {
            if (result != null) {
                getApplicationContext().getContentResolver().takePersistableUriPermission(
                        result,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );

                saveImage(result.toString());
                Toast.makeText(ImageActivity.this, "Изображение сохранено", Toast.LENGTH_SHORT).show();

                Timber.i("Image saved");
                Timber.i("SET IMG %s", result.toString());

            }
        }).launch(new String[]{"image/*"}));
    }


    public void changeGif(){
        gifButton.setOnClickListener(v -> show(new GiphyDialogFragment.GifSelectionListener() {

            @Override
            public void didSearchTerm(@NotNull String s) {
            }

            @Override
            public void onDismissed(@NotNull GPHContentType gphContentType) {
            }

            @Override
            public void onGifSelected(@NotNull Media media, @Nullable String s, @NotNull GPHContentType gphContentType) {
                // To fetch a particular id
                GPHCore.INSTANCE.gifById(media.getId(), (mediaResponse, throwable) -> {
                    ImageView imageView = findViewById(R.id.imageViewNote);
                    imageView.setVisibility(View.VISIBLE);
                    saveImage(Objects.requireNonNull(Objects.requireNonNull(mediaResponse.getData()).getImages().getOriginal()).getGifUrl());
                    Toast.makeText(ImageActivity.this, "Гифка сохранена", Toast.LENGTH_SHORT).show();
                    return null;
                });
            }
        }));
    }

    public void removeImage() {
        FloatingActionButton removeButton = findViewById(R.id.removeImageButton);
        removeButton.setOnClickListener(v -> {
            if (imageView.getDrawable() != null) {
                imageView.setImageDrawable(null);
                try {
                    Note note = imageViewModel.getNoteById(noteId);
                    note.setTextNote(noteText);
                    note.setTitle(noteTitle);
                    note.setImage(null);
                    imageViewModel.saveEditedNote(note);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                Toast.makeText(ImageActivity.this, "Изображение удалено", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveImage(String result) {
        Glide.with(ImageActivity.this).load(result).into(imageView);
        Note note = imageViewModel.getNoteById(noteId);
        note.setTextNote(noteText);
        note.setTitle(noteTitle);
        note.setImage(result);
        imageViewModel.saveEditedNote(note);
        Log.e(TAG, "old Image saved");
        isChanged = true;
    }

    @NonNull
    public GiphyDialogFragment show(@NonNull final  GiphyDialogFragment.GifSelectionListener listener)
    {
        final GPHSettings settings = new GPHSettings();
        settings.setTheme(GPHTheme.Dark);
        settings.setRenditionType(RenditionType.fixedWidth);
        settings.setGridType(GridType.waterfall);
        settings.setShowCheckeredBackground(false);

        final GPHContentType[] contentTypes = new GPHContentType[2];
        contentTypes[1] = GPHContentType.gif;
        contentTypes[0] = GPHContentType.recents;
        settings.setMediaTypeConfig(contentTypes);
        settings.setSelectedContentType(GPHContentType.gif);

        settings.setGridType(GridType.waterfall);
        settings.setStickerColumnCount(3);
        final GiphyDialogFragment dialog = GiphyDialogFragment.Companion.newInstance(settings);
        dialog.setGifSelectionListener(listener);
        dialog.show(getSupportFragmentManager(), "giphy_dialog");

        return dialog;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}
