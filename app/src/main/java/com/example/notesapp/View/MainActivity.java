package com.example.notesapp.View;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.notesapp.R;
import com.example.notesapp.ViewModel.MainViewModel;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private MainAdapter mainAdapter;
    private String NOTE_KEY = "Notes";
    private RecyclerView rcView;
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color=\"black\">" + getString(R
                .string.app_name) + "</font>")); // Перекраска заголовка Actionbar
        this.mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        try {
            mainAdapter = new MainAdapter(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() { // Инициализация необходимых элементов активити и прочего

        try {
            mainViewModel.init();
            rcView = findViewById(R.id.rcView);
            mainViewModel.getNotes().observe(this, notes -> {
                if (notes != null){
                    mainViewModel.setDisplayList(notes);
                    mainAdapter.updateAdapter(notes);
                }
            });

            rcView.setAdapter(mainAdapter);
            rcView.setLayoutManager(new LinearLayoutManager(this));
            getItemTouchHelper().attachToRecyclerView(rcView);
        }
        catch (Exception e){
            Log.i(TAG, "IllegalAccessException");
            //mainViewModel.logout();
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
        }
    }



    private void onLogout() { // метод для выхода из аккаунта
        mainViewModel.logout();
        startActivity(new Intent(MainActivity.this, AuthActivity.class));
        finish();
    }

    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { // Удаление по свайпу

                        mainViewModel.deleteNote(viewHolder.getAdapterPosition());
                        mainAdapter.updateAdapter(mainViewModel.getNotes().getValue());
                        //mainAdapter.notifyDataSetChanged();
                    }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addNote) { // обработка нажатия создания новой заметки
            startActivity(new Intent(MainActivity.this, EditActivity.class));
        } else if (item.getItemId() == R.id.logout) {
            onLogout(); // обработка нажатия кнопки выхода из аккаунта
        }
        return true;
    }

}