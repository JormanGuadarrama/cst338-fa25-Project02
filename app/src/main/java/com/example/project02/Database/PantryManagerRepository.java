package com.example.project02.Database;

import android.app.Application;
import android.util.Log;

import com.example.project02.Database.Entities.Pantry;
import com.example.project02.Database.Entities.User;
import com.example.project02.MainActivity;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PantryManagerRepository {
    private final PantryDAO pantryDAO;
    private final UserDAO userDAO;

    public PantryManagerRepository(Application application) {
        PantryManagerDatabase db = PantryManagerDatabase.getDatabase(application);
        this.pantryDAO = db.pantryDAO();
        this.userDAO = db.userDAO();
    }

    public ArrayList<Pantry> getAllLogs() {
        Future<ArrayList<Pantry>> future = PantryManagerDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<Pantry>>() {
                    @Override
                    public ArrayList<Pantry> call() throws Exception {
                        return (ArrayList<Pantry>) pantryDAO.getAllRecords();
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting all Pantries in the repository");
        }
        return null;
    }

    public User getUserByUsername(String username) {
        Future<User> future = PantryManagerDatabase.databaseWriteExecutor.submit(
                new Callable<User>() {
                    @Override
                    public User call() throws Exception {
                        return userDAO.getUserByUsername(username);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting user by username");
        }
        return null;
    }

    public void insertPantry(Pantry pantry) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            pantryDAO.insert(pantry);
        });
    }

    public void insertUser(User... user) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }
}
