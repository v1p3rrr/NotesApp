package com.example.notesapp.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Note implements Serializable {
    @PrimaryKey (autoGenerate = true)
    public int noteId;

    @ColumnInfo
    private String title;

    @ColumnInfo
    private String textNote;

    public Note(String title, String textNote){
        this.title = title;
        this.textNote = textNote;
    }

    public Note(int id, String title, String textNote){
        this.title = title;
        this.textNote = textNote;
        this.noteId = id;
    }


    public String getTitle(){
        return this.title;
    }

    public String getTextNote() {
        return textNote;
    }

    public int getNoteId() { return noteId; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTextNote(String textNote) {
        this.textNote = textNote;
    }
}
