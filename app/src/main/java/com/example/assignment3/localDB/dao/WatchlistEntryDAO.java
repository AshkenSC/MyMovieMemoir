package com.example.assignment3.localDB.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.example.assignment3.localDB.entity.WatchlistEntry;

@Dao
public interface WatchlistEntryDAO {

    @Query("SELECT * FROM WatchlistEntry")
    List<WatchlistEntry> getAll();

    @Insert
    void insertAll(WatchlistEntry... watchListEntries);

    @Insert
    long insert(WatchlistEntry watchListEntry);

    @Delete
    void delete(WatchlistEntry watchListEntry);

    @Query("DELETE FROM WatchlistEntry")
    void deleteAll();

}
