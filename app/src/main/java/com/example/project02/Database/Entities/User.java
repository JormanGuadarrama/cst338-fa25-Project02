package com.example.project02.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project02.Database.PantryManagerDatabase;

@Entity(tableName = PantryManagerDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;
    private boolean isAdmin;

}
