package com.example.project02.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project02.Database.Entities.PantryItem;

import java.util.List;

@Dao
public interface PantryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PantryItem pantryItem);

    @Update
    void update(PantryItem pantryItem);

    @Query("SELECT * FROM " + PantryManagerDatabase.PANTRY_TABLE)
    List<PantryItem> getAllRecords();

    @Query("SELECT * FROM " + PantryManagerDatabase.PANTRY_TABLE + " WHERE userId = :userId")
    List<PantryItem> getPantryByUserId(int userId);

    @Query("SELECT * FROM " + PantryManagerDatabase.PANTRY_TABLE + " WHERE userId = :userId AND foodId = :foodId")
    PantryItem getPantryItem(int userId, int foodId);
}
