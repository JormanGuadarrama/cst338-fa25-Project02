package com.example.project02;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project02.Database.Entities.User;
import com.example.project02.Database.PantryManagerDatabase;
import com.example.project02.Database.UserDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UserDatabaseTest {
    private UserDAO userDAO;
    private PantryManagerDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, PantryManagerDatabase.class).build();
        userDAO = db.userDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void createUser() throws Exception {
        User user = new User("testUser", "password");
        userDAO.insert(user);
        User byName = userDAO.getUserByUsername("testUser");
        assertNotNull(byName);
        assertEquals(user.getUsername(), byName.getUsername());
        assertEquals(user.getPassword(), byName.getPassword());
    }

    @Test
    public void deleteUser() throws Exception {
        User user = new User("deleteUser", "password");
        userDAO.insert(user);
        
        User insertedUser = userDAO.getUserByUsername("deleteUser");
        assertNotNull(insertedUser);
        
        userDAO.delete(insertedUser);
        
        User deletedUser = userDAO.getUserByUsername("deleteUser");
        assertNull(deletedUser);
    }
}
