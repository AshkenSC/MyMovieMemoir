package com.example.assignment3.localDB.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.assignment3.localDB.dao.WatchlistEntryDAO;
import com.example.assignment3.localDB.entity.WatchlistEntry;

@Database(entities = {WatchlistEntry.class}, version = 2, exportSchema = false)
public abstract class WatchlistEntryDatabase extends RoomDatabase {
    public abstract WatchlistEntryDAO WatchlistEntryDao();
    private static WatchlistEntryDatabase INSTANCE;
    public static synchronized WatchlistEntryDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WatchlistEntryDatabase.class, "WatchlistEntryDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
