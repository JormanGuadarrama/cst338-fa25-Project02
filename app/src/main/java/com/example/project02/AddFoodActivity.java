package com.example.project02;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project02.Database.Entities.Food;
import com.example.project02.Database.PantryManagerDatabase;
import com.example.project02.databinding.ActivityAddFoodBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class AddFoodActivity extends AppCompatActivity {

    private ActivityAddFoodBinding binding;
    private PantryManagerDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = PantryManagerDatabase.getDatabase(this);

        setupFamilyDropdown();

        binding.saveFoodButton.setOnClickListener(v -> saveFoodItem());
    }

    private void setupFamilyDropdown() {
        ExecutorService executor = PantryManagerDatabase.databaseWriteExecutor;
        executor.execute(() -> {
            List<String> families = db.foodDAO().getDistinctFamilies();
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, families);
                binding.foodFamilyAutocomplete.setAdapter(adapter);
            });
        });
    }

    private void saveFoodItem() {
        String name = binding.foodNameEdittext.getText().toString().trim();
        String description = binding.foodDescriptionEdittext.getText().toString().trim();
        String family = binding.foodFamilyAutocomplete.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            binding.foodNameEdittext.setError("Food name cannot be empty");
            return;
        }

        Food newFood = new Food(name, family, description);

        ExecutorService executor = PantryManagerDatabase.databaseWriteExecutor;
        executor.execute(() -> {
            db.foodDAO().insert(newFood);
            runOnUiThread(() -> {
                Toast.makeText(this, "Food item saved", Toast.LENGTH_SHORT).show();
                finish(); // Go back to the previous activity
            });
        });
    }
}
