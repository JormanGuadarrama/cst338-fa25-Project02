
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

    private EditText usernameField, passwordField;
    private TextView errorMessage;
    private PantryManagerRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        repository = new PantryManagerRepository(getApplication());

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        errorMessage = findViewById(R.id.errorMessage);
        Button loginButton = findViewById(R.id.loginButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);

        loginButton.setOnClickListener(v -> handleLogin());
        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
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
            showError("Invalid username or password.");
        }
    }

    private void showError(String msg) {
        errorMessage.setText(msg);
        errorMessage.setVisibility(View.VISIBLE);
    }
}
