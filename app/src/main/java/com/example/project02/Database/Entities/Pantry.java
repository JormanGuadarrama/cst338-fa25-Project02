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

    public Pantry() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateCreated = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pantry that = (Pantry) o;
        return id == that.id && Objects.equals(dateCreated, that.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateCreated);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}
