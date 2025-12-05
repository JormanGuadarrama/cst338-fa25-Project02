package com.example.project02.Database;

import android.app.Application;
import android.util.Log;

import com.example.project02.Database.Entities.Food;
import com.example.project02.Database.Entities.PantryItem;
import com.example.project02.Database.Entities.User;
import com.example.project02.MainActivity;

import java.util.ArrayList;
import java.util.List;
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

    public Long insertUser(User user) {
        Future<Long> future = PantryManagerDatabase.databaseWriteExecutor.submit(
                new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        return userDAO.insert(user);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when inserting user");
        }
        return null;
    }

    public List<PantryItem> getPantryByUserId(int userId) {
        Future<List<PantryItem>> future = PantryManagerDatabase.databaseWriteExecutor.submit(
                new Callable<List<PantryItem>>() {
                    @Override
                    public List<PantryItem> call() throws Exception {
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

    public ArrayList<PantryItem> getAllLogs() {
        Future<ArrayList<PantryItem>> future = PantryManagerDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<PantryItem>>() {
                    @Override
                    public ArrayList<PantryItem> call() throws Exception {
                        return (ArrayList<PantryItem>) pantryDAO.getAllRecords();
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
                        return (ArrayList<Food>) foodDAO.getAllFood();
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

    public ArrayList<User> getAllUsers() {
        Future<ArrayList<User>> future = PantryManagerDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<User>>() {
                    @Override
                    public ArrayList<User> call() throws Exception {
                        return (ArrayList<User>) userDAO.getAllUsers();
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting all Users in the repository");
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

    public void addItemToPantry(int userId, int foodId) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            PantryItem existingItem = pantryDAO.getPantryItem(userId, foodId);
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + 1);
                pantryDAO.update(existingItem);
            } else {
                PantryItem newItem = new PantryItem(userId, foodId);
                newItem.setQuantity(1);
                pantryDAO.insert(newItem);
            }
        });
    }

    public void insertUserVoid(User... users) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            for (User user : users) {
                userDAO.insert(user);
            }
        });
    }

    public void deleteUser(User user) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.delete(user);
        });
    }

    public void insertFood(Food... food) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            foodDAO.insert(food);
        });
    }

    public void deleteFood(Food food) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            foodDAO.delete(food);
        });
    }

    public void deleteUserLogs(User user) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            pantryDAO.deleteLogs(user.getId());
        });
    }
}
