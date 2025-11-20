package com.example.project02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private TextView errorMessage;
    private UserRepository userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRepo = new UserRepository();

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

        User user = userRepo.authenticate(username, password);
        if (user != null) {
            errorMessage.setVisibility(View.GONE);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
