package com.example.project02;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.project02.Database.Entities.User;
import com.example.project02.Database.PantryManagerDatabase;
import com.example.project02.Database.UserDAO;
import com.example.project02.databinding.ActivityLandingPageBinding;
import com.example.project02.viewmodel.UserViewModel;

import java.util.concurrent.Executors;

public class LandingPage extends AppCompatActivity {

    private ActivityLandingPageBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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
                    userViewModel.selectUser(user); // Share the user
                    binding.homeUsernameText.setText("User: " + user.getUsername());
                    if (user.isAdmin()) {
                        binding.adminControlButton.setVisibility(View.VISIBLE);
                        binding.adminControlButton.setOnClickListener(v -> {
                            startActivity(adminControlsActivity.getIntent(LandingPage.this, user.getId()));
                        });
                    }
                } else {
                    binding.homeUsernameText.setText("User: Unknown");
                }
            });
        });
    }
}
