package com.example.project02.Database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project02.Database.Entities.Food;
import com.example.project02.Database.Entities.PantryItem;
import com.example.project02.Database.Entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class PantryDaoTest {

    private PantryManagerDatabase db;
    private PantryDAO pantryDAO;
    private UserDAO userDAO;
    private FoodDAO foodDAO;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, PantryManagerDatabase.class)
                 .allowMainThreadQueries()
                 .build();
        pantryDAO = db.pantryDAO();
        userDAO = db.userDAO();
        foodDAO = db.foodDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void addAndGetPantryItem() {
        User user = new User("testuser", "password");
        long userId = userDAO.insert(user);

        Food food = new Food("Banana", "Fruit", "A yellow fruit");
        foodDAO.insert(food);
        Food insertedFood = foodDAO.getFoodByFamily("Fruit");
        assertNotNull(insertedFood);

        PantryItem pantryItem = new PantryItem((int)userId, insertedFood.getId());
        pantryDAO.insert(pantryItem);

        List<PantryItemWithFood> userPantry = pantryDAO.getPantryItemsWithFood((int)userId);

        assertEquals(1, userPantry.size());
        assertEquals("Banana", userPantry.get(0).food.getName());
        assertEquals(1, userPantry.get(0).pantryItem.getQuantity());
    }

    @Test
    public void getAllFood_returnsAllFood() {
        Food food1 = new Food("Test Food 1", "Test Family", "Desc 1");
        Food food2 = new Food("Test Food 2", "Test Family", "Desc 2");
        foodDAO.insert(food1, food2);

        List<Food> allFoods = foodDAO.getAllFood();

        assertNotNull(allFoods);
        assertEquals(2, allFoods.size());
    }
}
