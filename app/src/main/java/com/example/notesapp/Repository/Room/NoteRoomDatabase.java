package com.example.notesapp.Repository.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Repository.Room.DAO.NoteDAO;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteRoomDatabase extends RoomDatabase {
    private static NoteRoomDatabase instance;

    public abstract NoteDAO getNoteDAO();

    public static RoomDatabase getRoomDatabaseInstance(final Context context) {
        if (instance == null)
            synchronized (NoteRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            NoteRoomDatabase.class, "partyApp_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        return instance;
    }
}
