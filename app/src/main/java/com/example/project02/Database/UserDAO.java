package com.example.project02.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project02.Database.Entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + PantryManagerDatabase.USER_TABLE + " ORDER BY username")
    List<User> getAllUsers();

    @Query("SELECT * FROM " + PantryManagerDatabase.USER_TABLE + " WHERE username = :username")
    User getUserByUsername(String username);

    @Query("DELETE FROM " + PantryManagerDatabase.USER_TABLE)
    void deleteAll();
}
