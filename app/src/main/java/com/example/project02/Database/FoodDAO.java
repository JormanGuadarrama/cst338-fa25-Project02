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
    List<Food> getAllFood();

    @Query("SELECT DISTINCT family FROM " + PantryManagerDatabase.FOODS_TABLE + " ORDER BY family ASC")
    List<String> getDistinctFamilies();

    @Query("SELECT * FROM " + PantryManagerDatabase.FOODS_TABLE + " WHERE id = :id")
    Food getFoodById(int id);

    @Query("SELECT * FROM " + PantryManagerDatabase.FOODS_TABLE + " WHERE family = :family")
    Food getFoodByFamily(String family);

    @Query("SELECT * FROM " + PantryManagerDatabase.FOODS_TABLE + " WHERE name LIKE '%' || :text || '%'")
    List<Food> searchSuggest(String text);
}
