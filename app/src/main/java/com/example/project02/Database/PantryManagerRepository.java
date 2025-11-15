package com.example.project02.Database;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;

import com.example.project02.Database.Entities.Pantry;
import com.example.project02.MainActivity;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PantryManagerRepository {
    private PantryDAO pantryDAO;
    private ArrayList<Pantry> allLogs;

    public PantryManagerRepository(Application application) {
        PantryManagerDatabase db = PantryManagerDatabase.getDatabase(application);
        this.pantryDAO = db.pantryDAO();
        this.allLogs = this.pantryDAO.getAllRecords();

    }

    public ArrayList<Pantry> getAllLogs() {
        Future<ArrayList<Pantry>> future = PantryManagerDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<Pantry>>() {
                    @Override
                    public ArrayList<Pantry> call() throws Exception {
                        return pantryDAO.getAllRecords();
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

    public void insertPantry(Pantry pantry) {
        PantryManagerDatabase.databaseWriteExecutor.execute(() -> {
            pantryDAO.insert(pantry);
        });
    }
}
