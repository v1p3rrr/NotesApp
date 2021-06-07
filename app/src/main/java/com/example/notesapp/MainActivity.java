package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //private ListView listView;
    private MainAdapter mainAdapter;
    //private ArrayAdapter<String> adapter;
    private List<Note> listData;
    private List<String> listId;
    private DatabaseReference noteDataBase;
    private String NOTE_KEY = "Note";
    private RecyclerView rcView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        readFromDb();
        mainAdapter.updateAdapter(listData, listId);

        //onClickEdit();
    }

    private void init() {
        //listView = findViewById(R.id.listView);
        rcView = findViewById(R.id.rcView);
        listData = new ArrayList<>();
        listId = new ArrayList<>();
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mainAdapter = new MainAdapter(this);
        rcView.setAdapter(mainAdapter);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        //listView.setAdapter(adapter);
        noteDataBase = FirebaseDatabase.getInstance().getReference(NOTE_KEY);
        getItemTouchHelper().attachToRecyclerView(rcView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        readFromDb();
        mainAdapter.updateAdapter(listData, listId);
    }

    private void readFromDb() {
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

//    private void onClickEdit() {
//        rcView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(MainActivity.this, EditActivity.class);
//                i.putExtra("noteId", listId.get(position));
//                startActivity(i);
//            }
//        });
//    }

    public void onClickAdd(View view) {
        Intent i = new Intent(MainActivity.this, EditActivity.class);
        startActivity(i);
    }

    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteDataBase.child(listId.get(viewHolder.getAdapterPosition())).removeValue();
                listData.remove(viewHolder.getAdapterPosition());
                listId.remove(viewHolder.getAdapterPosition());
                mainAdapter.updateAdapter(listData, listId);
                mainAdapter.notifyDataSetChanged();
            }
        });
    }
}