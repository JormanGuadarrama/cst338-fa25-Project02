package com.example.project02.ui.list;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project02.Database.Entities.Food;
import com.example.project02.Database.PantryManagerDatabase;
import com.example.project02.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";

    private RecyclerView foodRecyclerView;
    private FoodListAdapter foodListAdapter;
    private PantryManagerDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        foodRecyclerView = view.findViewById(R.id.food_recyclerview);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodListAdapter = new FoodListAdapter(new ArrayList<>());
        foodRecyclerView.setAdapter(foodListAdapter);

        db = PantryManagerDatabase.getDatabase(getContext());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFoodItems();
    }

    private void loadFoodItems() {
        ExecutorService executor = PantryManagerDatabase.databaseWriteExecutor;
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Background work to fetch all food items
            List<Food> foodList = db.foodDAO().getAllFood();
            Log.d(TAG, "Number of food items loaded from DB: " + (foodList != null ? foodList.size() : "null"));

            // Post result back to the main thread to update the UI
            handler.post(() -> {
                Log.d(TAG, "Updating RecyclerView on main thread.");
                foodListAdapter.setFoodList(foodList);
            });
        });
    }
}
