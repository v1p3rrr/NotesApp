package com.example.notesapp;

import java.io.Serializable;

public class Note implements Serializable {
    public String title, textNote;

    public Note(){

    }

    public Note(String title, String textNote){
        this.title = title;
        this.textNote = textNote;
    }
}
