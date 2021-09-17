package com.example.notesapp.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.notesapp.Data.Note;
import com.example.notesapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainAdapter mainAdapter;
    private List<Note> listData;
    private List<String> listId;
    private List<String> adminList;
    private DatabaseReference noteDataBase;
    private String NOTE_KEY = "Note";
    private RecyclerView rcView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R
                .string.app_name) + "</font>")); // Перекраска заголовка Actionbar
        init();
        readFromDb();
        mainAdapter.updateAdapter(listData, listId);
    }

    private void init() { // Инициализация необходимых элементов активити и прочего
        rcView = findViewById(R.id.rcView);
        adminList = new ArrayList<>();
        listData = new ArrayList<>();
        listId = new ArrayList<>();
        mainAdapter = new MainAdapter(this);
        rcView.setAdapter(mainAdapter);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        noteDataBase = FirebaseDatabase.getInstance().getReference(NOTE_KEY);
        checkAdmins();
    }

    private void checkAdmins() { // Проверяет, является ли нынешний пользователь админом, указанным в БД
        DatabaseReference admRef = FirebaseDatabase.getInstance().getReference("Adminlist");

        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (adminList.size() > 0) adminList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        assert ds.getValue(String.class) != null;
                        adminList.add(ds.getValue(String.class));
                    }
                    String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    assert currUser != null;
                    for (String a : adminList) {
                        if (a.equals(currUser)) {
                            getItemTouchHelper().attachToRecyclerView(rcView); // Если админ,
                            // подключает тачхелпер, позволяющий удалять элементы по свайпу
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //System.out.println("No admins detected");
            }
        };
        admRef.addListenerForSingleValueEvent(vListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        readFromDb();
        mainAdapter.updateAdapter(listData, listId);
    }

    private void readFromDb() { // Чтение заметок из БД и обновление списка
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listData.size() > 0) listData.clear();
                if (listId.size() > 0) listId.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Note note = ds.getValue(Note.class);
                    String id = ds.getKey();
                    assert note != null;
                    listId.add(id);
                    listData.add(note);
                }
                mainAdapter.updateAdapter(listData, listId);
                mainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Ошибка чтения из базы данных", Toast.LENGTH_SHORT).show();
            }
        };
        noteDataBase.addValueEventListener(vListener);
    }

    private void onLogout() { // метод для выхода из аккаунта
        FirebaseAuth.getInstance().signOut();
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
                noteDataBase.child(listId.get(viewHolder.getAdapterPosition())).removeValue();
                listData.remove(viewHolder.getAdapterPosition());
                listId.remove(viewHolder.getAdapterPosition());
                mainAdapter.updateAdapter(listData, listId);
                mainAdapter.notifyDataSetChanged();
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