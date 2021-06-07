package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private Context context;
    private List<Note> mainArray;
    private List<String> listId;


    public MainAdapter(Context context) {
        this.context = context;
        mainArray = new ArrayList<>();
        listId = new ArrayList<>();
    }

    @NonNull
    @Override // todo commentaries
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_list_layout, parent, false);
        return new MyViewHolder(view, context, mainArray, listId);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(mainArray.get(position).title);
    }

    @Override
    public int getItemCount() {
        return mainArray.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle;
        private final Context context;
        private final List<Note> mainArray;
        private final List<String> listId;


        public MyViewHolder(@NonNull View itemView, Context context, List<Note> mainArray, List<String> listId) {
            super(itemView);
            this.context = context;
            this.mainArray = mainArray;
            this.listId = listId;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);

        }

        public void setData(String title) {
            tvTitle.setText(title);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, EditActivity.class);
            i.putExtra("noteId", listId.get(getAdapterPosition()));
            context.startActivity(i);
        }
    }

    public void updateAdapter(List<Note> newList, List<String> ids) {
        mainArray.clear();
        listId.clear();
        mainArray.addAll(newList);
        listId.addAll(ids);
        notifyDataSetChanged();
    }
}