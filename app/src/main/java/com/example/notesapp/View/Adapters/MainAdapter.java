package com.example.notesapp.View.Adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Data.Note;
import com.example.notesapp.R;
import com.example.notesapp.View.EditActivity;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private final Context context;
    private List<Note> mainArray;


    public MainAdapter(Context context) throws IllegalAccessException { // Адаптер, отвечающий за заполнение списка recyclerView
        this.context = context;
        mainArray = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Создание
        View view = LayoutInflater.from(context).inflate(R.layout.note_list_layout, parent, false);
        return new MyViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) { // Заполнение
        holder.setData(mainArray.get(position));
    }

    @Override
    public int getItemCount() {
        return mainArray.size();
    }

    public Note getNote(int position){
        return mainArray.get(position);
    }

    // Класс, отвечающий за отдельный элемент
    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle;
        private final Context context;
        private Note localNote;


        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        public void setData(Note note) {
            localNote = note;
            tvTitle.setText(note.getTitle()); // заполнение заголовка для элемента списка
        }

        @Override
        public void onClick(View v) { // при нажатии на элемент передается ID заметки из бд и переходит на экран изменения
            Intent i = new Intent(context, EditActivity.class);
            i.putExtra("noteId", localNote.noteId);
            context.startActivity(i);
        }
    }

    public void updateAdapter(List<Note> newList) { // Обновление списка
        mainArray = newList;
        //notifyDataSetChanged();
    }
}