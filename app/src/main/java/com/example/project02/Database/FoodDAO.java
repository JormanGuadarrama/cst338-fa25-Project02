package com.example.project02.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project02.Database.Entities.Food;

import java.util.List;

@Dao
public interface FoodDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Food... food);

    @Delete
    void delete(Food food);

    @Query("SELECT * FROM " + PantryManagerDatabase.FOODS_TABLE)
    List<Food> getAllFoods();

    @Query("SELECT * FROM " + PantryManagerDatabase.FOODS_TABLE + " WHERE id = :id")
    Food getFoodById(int id);
}
