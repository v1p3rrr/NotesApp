package com.example.notesapp.Data;

import java.io.Serializable;

public class Note implements Serializable {
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
