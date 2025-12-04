package com.example.project02.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project02.Database.Entities.Food;
import com.example.project02.Database.Entities.PantryItem;
import com.example.project02.Database.Entities.User;
import com.example.project02.Database.PantryManagerDatabase;
import com.example.project02.R;
import com.example.project02.util.IntentFactory;
import com.example.project02.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ListFragment extends Fragment implements FoodListAdapter.OnItemAddListener {

    private static final String TAG = "ListFragment";

    private FoodListAdapter foodListAdapter;
    private PantryManagerDatabase db;
    private UserViewModel userViewModel;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        db = PantryManagerDatabase.getDatabase(getContext());
        RecyclerView foodRecyclerView = view.findViewById(R.id.food_recyclerview);
        Button addFoodButton = view.findViewById(R.id.add_food_item_button);

        setupRecyclerView(foodRecyclerView);
        setupAddFoodButton(addFoodButton);
        observeUser();

        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodListAdapter = new FoodListAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(foodListAdapter);
    }

    private void setupAddFoodButton(Button button) {
        button.setOnClickListener(v -> {
            Intent intent = IntentFactory.getAddFoodActivityIntent(getContext());
            startActivity(intent);
        });
    }

    private void observeUser() {
        userViewModel.getSelectedUser().observe(getViewLifecycleOwner(), user -> {
            currentUser = user;
            if (user != null) {
                Log.d(TAG, "Current user set in ListFragment: " + user.getUsername());
            }
        });
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
            List<Food> foodList = db.foodDAO().getAllFood();
            Log.d(TAG, "Number of food items loaded from DB: " + (foodList != null ? foodList.size() : "null"));
            handler.post(() -> {
                Log.d(TAG, "Updating RecyclerView on main thread.");
                foodListAdapter.setFoodList(foodList);
            });
        });
    }

    @Override
    public void onItemAdd(Food food) {
        if (currentUser == null) {
            Toast.makeText(getContext(), "Error: User not logged in.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Add item clicked but currentUser is null.");
            return;
        }

        Log.d(TAG, "Adding '" + food.getName() + "' to pantry for user '" + currentUser.getUsername() + "'");

        ExecutorService executor = PantryManagerDatabase.databaseWriteExecutor;
        executor.execute(() -> {
            PantryItem item = db.pantryDAO().getPantryItem(currentUser.getId(), food.getId());
            if (item == null) {
                item = new PantryItem(currentUser.getId(), food.getId());
                db.pantryDAO().insert(item);
            } else {
                item.setQuantity(item.getQuantity() + 1);
                db.pantryDAO().update(item);
            }
            
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(getContext(), "Added " + food.getName() + " to pantry", Toast.LENGTH_SHORT).show();
            });
        });
    }
}
