package com.example.project02.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project02.Database.PantryManagerRepository;
import com.example.project02.R;
import com.example.project02.Database.PantryManagerDatabase;
import com.example.project02.Database.UserDAO;
import com.example.project02.Database.Entities.User;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateAccountActivity extends AppCompatActivity {

    private TextInputLayout usernameLayout, passwordLayout, confirmLayout;
    private TextInputEditText usernameField, passwordField, confirmField;
    private MaterialSwitch adminSwitch;
    private TextView errorMessage;
    private MaterialButton createButton, cancelButton;

    private UserDAO userDao;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        PantryManagerDatabase db = PantryManagerDatabase.getInstance(getApplicationContext());
        userDao = db.UserDAO();

        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmLayout  = findViewById(R.id.confirmLayout);

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        confirmField  = findViewById(R.id.confirmField);

        adminSwitch = findViewById(R.id.adminSwitch);
        errorMessage = findViewById(R.id.errorMessage);
        createButton = findViewById(R.id.createButton);
        cancelButton = findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(v -> finish());
        createButton.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String username = safeText(usernameField);
        String password = safeText(passwordField);
        String confirm  = safeText(confirmField);
        boolean isAdmin = adminSwitch.isChecked();

        clearErrors();

        if (username.isEmpty()) {
            usernameLayout.setError("Username required");
            return;
        }
        if (password.length() < 4) {
            passwordLayout.setError("Password must be at least 4 characters");
            return;
        }
        if (!password.equals(confirm)) {
            confirmLayout.setError("Passwords do not match");
            return;
        }

        createButton.setEnabled(false);

        io.execute(() -> {
            try {

                User existing = userDao.findByUsername(username);
                if (existing != null) {
                    runOnUiThread(() -> {
                        createButton.setEnabled(true);
                        showError("Username already exists");
                    });
                    return;
                }

                User user = new User(username, password);
                user.setAdmin(isAdmin);
                userDao.insert(user);

                PantryManagerRepository repo = new PantryManagerRepository(getApplication());
                repo.insertUser(user);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    createButton.setEnabled(true);
                    showError("Failed to create account: " + e.getMessage());
                });
            }
        });
    }

    private String safeText(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private void clearErrors() {
        usernameLayout.setError(null);
        passwordLayout.setError(null);
        confirmLayout.setError(null);
        errorMessage.setVisibility(View.GONE);
    }

    private void showError(String msg) {
        errorMessage.setText(msg);
        errorMessage.setVisibility(View.VISIBLE);
    }
}
