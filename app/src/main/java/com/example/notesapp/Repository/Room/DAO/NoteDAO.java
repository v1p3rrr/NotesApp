package com.example.notesapp.Repository.Room.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notesapp.Data.Note;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert
    void addNote(Note note);

    @Delete
    void deleteNote (Note note);

    @Update
    void updateNote (Note note);

    @Query("SELECT * FROM note")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note WHERE noteId = :id")
    Note getNoteById(int id);

    @Query("SELECT * FROM note WHERE noteId=(SELECT MAX(noteId) FROM note)")
    Note getLastNote();
}
