package com.example.notesapp;

import java.io.Serializable;

public class Note implements Serializable {
    public String id, title, textNote;

    public Note(){

    }

    public Note(String id, String title, String textNote){
        this.id = id;
        this.title = title;
        this.textNote = textNote;
    }
}
