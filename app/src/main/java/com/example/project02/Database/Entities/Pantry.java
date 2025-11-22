package com.example.project02.Database.Entities;


import android.os.Build;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project02.Database.PantryManagerDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = PantryManagerDatabase.PANTRY_TABLE)
public class Pantry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private LocalDateTime dateCreated;

    public Pantry(int userId) {
            dateCreated = LocalDateTime.now();
            this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pantry pantry = (Pantry) o;
        return id == pantry.id && userId == pantry.userId && Objects.equals(dateCreated, pantry.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, dateCreated);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}
