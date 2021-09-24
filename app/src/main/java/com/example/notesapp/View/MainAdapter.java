package com.example.notesapp.View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Data.Note;
import com.example.notesapp.R;

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
        holder.setData(mainArray.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mainArray.size();
    }

    // Класс, отвечающий за отдельный элемент
    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle;
        private final Context context;


        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        public void setData(String title) {
            tvTitle.setText(title); // заполнение заголовка для элемента списка
        }

        @Override
        public void onClick(View v) { // при нажатии на элемент передается ID заметки из бд и переходит на экран изменения
            Intent i = new Intent(context, EditActivity.class);
            i.putExtra("noteId", getAdapterPosition());
            context.startActivity(i);
        }
    }

    public void updateAdapter(List<Note> newList) { // Обновление списка
        mainArray = newList;
        //notifyDataSetChanged();
    }
}