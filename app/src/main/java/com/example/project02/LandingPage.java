package com.example.project02;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project02.Database.PantryManagerDatabase;
import com.example.project02.Database.UserDAO;
import com.example.project02.Database.Entities.User;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
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
        TextView usernameTextView = findViewById(R.id.homeUsernameText);

        // New thread for db access
        Executors.newSingleThreadExecutor().execute(() -> {
            // Retrieve user from database
            User user = userDAO.getUserByUsername(getIntent().getStringExtra("username"));

            runOnUiThread(() -> {
                if (user != null) {
                    usernameTextView.setText("User: " + user.getUsername());
                } else {
                    usernameTextView.setText("User: Unknown");
                }

                Button adminButton = findViewById(R.id.adminControlButton);
                if (user.isAdmin()) {
                    adminButton.setVisibility(View.VISIBLE);
                }
            });
        });
    }
}
