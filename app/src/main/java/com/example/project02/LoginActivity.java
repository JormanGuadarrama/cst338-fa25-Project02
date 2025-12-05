
//@author bhavishya//
//@since 25/11/2025//

package com.example.project02.ui;

import android.content.Intent;
import com.example.project02.ui.CreateAccountActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project02.LandingPage;

import com.example.project02.R;
import com.example.project02.Database.Entities.User;
import com.example.project02.Database.PantryManagerRepository;

public class LoginActivity extends AppCompatActivity {
    // UI elements

    private EditText usernameField, passwordField;
    // Error message to display
    private TextView errorMessage;
    // Repository to interact with the database
    private PantryManagerRepository repository;
    // Activity lifecycle methods


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        repository = new PantryManagerRepository(getApplication());
        // Initialize UI elements

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        errorMessage = findViewById(R.id.errorMessage);
        Button loginButton = findViewById(R.id.loginButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);
        // Set up click listeners

        loginButton.setOnClickListener(v -> handleLogin());
        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        // Handle login logic here
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        // Check if username and password are not empty

        if (username.isEmpty() || password.isEmpty()) {// Empty fields
            showError("Please enter both username and password.");
            return;
        }

        User user = repository.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            errorMessage.setVisibility(View.GONE);
            Intent intent = new Intent(LoginActivity.this, LandingPage.class);
            intent.putExtra("username", user.getUsername());
            startActivity(intent);
            finish();
        } else {
            // Invalid credentials
            showError("Invalid username or password.");
        }
    }

    private void showError(String msg) {
        // Display an error message
        errorMessage.setText(msg);
        errorMessage.setVisibility(View.VISIBLE);
    }
}
