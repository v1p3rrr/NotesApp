package com.example.notesapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.notesapp.Data.Note;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NotesTest {

    @Test
    public void getTitle() {
        Note note = new Note("title", "text");
        assertEquals("title",note.getTitle());
    }

    @Test
    public void getTextNote() {
        Note note = new Note("title", "text");
        assertEquals("text",note.getTextNote());
    }
}