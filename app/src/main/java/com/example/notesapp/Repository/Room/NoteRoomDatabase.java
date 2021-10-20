package com.example.notesapp.Repository.Room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.notesapp.Data.Note;
import com.example.notesapp.Repository.Room.DAO.NoteDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class}, version = 2)
public abstract class NoteRoomDatabase extends RoomDatabase {
    private static volatile NoteRoomDatabase instance;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final Migration migration = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Note ADD COLUMN image TEXT");
        }
    };

    public abstract NoteDAO noteDAO();

    public static NoteRoomDatabase getRoomDatabaseInstance(final Context context) {
        if (instance == null)
            synchronized (NoteRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), NoteRoomDatabase.class, "notesApp_database").addMigrations(migration).allowMainThreadQueries().build();
                }
            }
        return instance;
    }
}
