package com.example.project02.Database;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.example.project02.Database.Entities.Food;
import com.example.project02.Database.Entities.PantryItem;

public class PantryItemWithFood {
    @Embedded
    public PantryItem pantryItem;

    @Relation(
            parentColumn = "foodId",
            entityColumn = "id"
    )
    public Food food;
}
