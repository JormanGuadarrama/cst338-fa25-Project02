package com.example.project02.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project02.Database.Entities.Pantry;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PantryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Pantry pantry);

    @Query("SELECT * FROM " + PantryManagerDatabase.PANTRY_TABLE)
    ArrayList<Pantry> getAllRecords();
}
