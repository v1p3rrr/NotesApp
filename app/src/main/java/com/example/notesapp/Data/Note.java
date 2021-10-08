package com.example.notesapp.Data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Note implements Serializable {
    @PrimaryKey int noteId;
    private String title, textNote;

    public Note(String title, String textNote){
        this.title = title;
        this.textNote = textNote;
    }


    public String getTitle(){
        return this.title;
    }

    public String getTextNote() {
        return textNote;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTextNote(String textNote) {
        this.textNote = textNote;
    }
}
