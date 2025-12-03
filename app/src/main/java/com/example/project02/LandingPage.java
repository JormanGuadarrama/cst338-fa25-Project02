package com.example.project02;

import android.os.Bundle;
import android.view.View;

import com.example.project02.Database.PantryManagerDatabase;
import com.example.project02.Database.UserDAO;
import com.example.project02.Database.Entities.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.project02.databinding.ActivityLandingPageBinding;

import java.util.concurrent.Executors;

public class LandingPage extends AppCompatActivity {

    private ActivityLandingPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_landing_page);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.navView, navController);

        UserDAO userDAO = PantryManagerDatabase.getDatabase(this).userDAO();

        // New thread for db access
        Executors.newSingleThreadExecutor().execute(() -> {
            // Retrieve user from database
            User user = userDAO.getUserByUsername(getIntent().getStringExtra("username"));

            runOnUiThread(() -> {
                if (user != null) {
                    binding.homeUsernameText.setText("User: " + user.getUsername());
                    if (user.isAdmin()) {
                        binding.adminControlButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.homeUsernameText.setText("User: Unknown");
                }
            });
        });
    }
}
