package com.example.project02.Database.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.project02.Database.PantryManagerDatabase;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = PantryManagerDatabase.PANTRY_TABLE,
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Food.class,
                        parentColumns = "id",
                        childColumns = "foodId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "userId"),
                @Index(value = "foodId")
        }
)
public class Pantry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private int foodId;
    private LocalDateTime dateCreated;

    public Pantry(int userId, int foodId) {
        this.userId = userId;
        this.foodId = foodId;
        this.dateCreated = LocalDateTime.now();
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

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pantry pantry = (Pantry) o;
        return id == pantry.id && userId == pantry.userId && foodId == pantry.foodId && Objects.equals(dateCreated, pantry.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, foodId, dateCreated);
    }
}
