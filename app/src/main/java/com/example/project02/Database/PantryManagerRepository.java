package com.example.project02.Database;

import android.app.Application;
import android.util.Log;

import com.example.project02.Database.Entities.Food;
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
    private final FoodDAO foodDAO;

    public PantryManagerRepository(Application application) {
        PantryManagerDatabase db = PantryManagerDatabase.getDatabase(application);
        this.pantryDAO = db.pantryDAO();
        this.userDAO = db.userDAO();
        this.foodDAO = db.foodDAO();
    }

    public Long insertUserWithPantry(User user) {
        Future<Long> future = PantryManagerDatabase.databaseWriteExecutor.submit(
                new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        return userDAO.insertUserWithPantry(user);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when inserting user with pantry");
        }
        return null;
    }

    public Pantry getPantryByUserId(int userId) {
        Future<Pantry> future = PantryManagerDatabase.databaseWriteExecutor.submit(
                new Callable<Pantry>() {
                    @Override
                    public Pantry call() throws Exception {
                        return pantryDAO.getPantryByUserId(userId);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting pantry by user id");
        }
        return null;
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

    public ArrayList<Food> getAllFoods() {
        Future<ArrayList<Food>> future = PantryManagerDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<Food>>() {
                    @Override
                    public ArrayList<Food> call() throws Exception {
                        return (ArrayList<Food>) foodDAO.getAllFoods();
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting all Foods in the repository");
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

    public Food getFoodById(int id) {
        Future<Food> future = PantryManagerDatabase.databaseWriteExecutor.submit(
                new Callable<Food>() {
                    @Override
                    public Food call() throws Exception {
                        return foodDAO.getFoodById(id);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting food by id");
        }
        return null;
    }

    public void insertPantry(Pantry pantry) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            pantryDAO.insert(pantry);
        });
    }

    public void insertUser(User... users) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            for (User user : users) {
                userDAO.insertUserWithPantry(user);
            }
        });
    }

    public void insertFood(Food... food) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            foodDAO.insert(food);
        });
    }
}
