package com.example.notesapp.Repository;

import androidx.lifecycle.LiveData;

import com.example.notesapp.Data.Note;

import java.util.List;

public interface RepositoryTasks {
    <T extends Note> LiveData<List<T>> getAllNotes();
    void addNote(Note note);
    void deleteNote(Note note);
    void updateNote (Note note);
}

