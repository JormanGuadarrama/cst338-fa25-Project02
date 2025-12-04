package com.example.project02.ui.pantry;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project02.Database.Entities.PantryItem;
import com.example.project02.Database.Entities.User;
import com.example.project02.Database.PantryItemWithFood;
import com.example.project02.Database.PantryManagerDatabase;
import com.example.project02.R;
import com.example.project02.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class PantryFragment extends Fragment implements PantryListAdapter.OnItemRemoveListener {

    private static final String TAG = "PantryFragment";

    private PantryListAdapter pantryListAdapter;
    private PantryManagerDatabase db;
    private UserViewModel userViewModel;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        db = PantryManagerDatabase.getDatabase(getContext());
        RecyclerView pantryRecyclerView = view.findViewById(R.id.pantry_recyclerview);

        setupRecyclerView(pantryRecyclerView);
        observeUser();

        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pantryListAdapter = new PantryListAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(pantryListAdapter);
    }

    private void observeUser() {
        userViewModel.getSelectedUser().observe(getViewLifecycleOwner(), user -> {
            currentUser = user;
            if (user != null) {
                Log.d(TAG, "Current user set in PantryFragment: " + user.getUsername());
                loadPantryItems(); // Load items when user is confirmed
            } else {
                pantryListAdapter.setPantryList(new ArrayList<>()); // Clear list if user logs out
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // The observer will trigger the load, so we don't need to call it here explicitly
    }

    private void loadPantryItems() {
        if (currentUser == null) return;
        ExecutorService executor = PantryManagerDatabase.databaseWriteExecutor;
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<PantryItemWithFood> pantryList = db.pantryDAO().getPantryItemsWithFood(currentUser.getId());
            Log.d(TAG, "Loaded " + pantryList.size() + " items for user " + currentUser.getUsername());
            handler.post(() -> pantryListAdapter.setPantryList(pantryList));
        });
    }

    @Override
    public void onItemRemove(PantryItemWithFood item) {
        Log.d(TAG, "Removing '" + item.food.getName() + "' from pantry for user '" + currentUser.getUsername() + "'");

        ExecutorService executor = PantryManagerDatabase.databaseWriteExecutor;
        executor.execute(() -> {
            PantryItem pantryItem = item.pantryItem;
            if (pantryItem.getQuantity() > 1) {
                pantryItem.setQuantity(pantryItem.getQuantity() - 1);
                db.pantryDAO().update(pantryItem);
            } else {
                db.pantryDAO().delete(pantryItem);
            }
            // After db operation, reload the list on the main thread
            new Handler(Looper.getMainLooper()).post(this::loadPantryItems);
        });
    }
}
