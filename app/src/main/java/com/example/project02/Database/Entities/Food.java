package com.example.project02.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project02.Database.PantryManagerDatabase;

import java.util.Objects;

@Entity(tableName = PantryManagerDatabase.FOODS_TABLE)
public class Food {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String family;
    private String description;

    public Food(String name, String family, String description) {
        this.name = name;
        this.family = family;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return id == food.id && Objects.equals(name, food.name) && Objects.equals(family, food.family) && Objects.equals(description, food.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, family, description);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
