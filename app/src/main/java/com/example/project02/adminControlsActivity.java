package com.example.project02;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project02.Database.Entities.Food;
import com.example.project02.Database.Entities.User;
import com.example.project02.Database.PantryManagerRepository;
import com.example.project02.databinding.ActivityAdminControlsBinding;

import java.util.ArrayList;
import java.util.List;

public class adminControlsActivity extends AppCompatActivity {

    ActivityAdminControlsBinding binding;
    PantryManagerRepository repository;

    private User selectedUser;
    private Food selectedFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminControlsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = new PantryManagerRepository(getApplication());

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setUserDropdown();
        setFoodDropdown();

        binding.buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUser != null) {
                    repository.deleteUser(selectedUser);
                    Toast.makeText(adminControlsActivity.this, "User " + selectedUser.getUsername() + " deleted", Toast.LENGTH_SHORT).show();
                    setUserDropdown(); // Refresh list
                    selectedUser = null;
                } else {
                    Toast.makeText(adminControlsActivity.this, "No user selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonDeleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFood != null) {
                    repository.deleteFood(selectedFood);
                    Toast.makeText(adminControlsActivity.this, "Food " + selectedFood.getName() + " deleted", Toast.LENGTH_SHORT).show();
                    setFoodDropdown();
                    selectedFood = null;
                } else {
                    Toast.makeText(adminControlsActivity.this, "No food selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUserDropdown() {
        List<User> users = repository.getAllUsers();
        if (users == null) users = new ArrayList<>();
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerUsers.setAdapter(adapter);

        binding.spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = (User) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedUser = null;
            }
        });
    }

    private void setFoodDropdown() {
        List<Food> foods = repository.getAllFoods();
        if (foods == null) foods = new ArrayList<>();
        ArrayAdapter<Food> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFood.setAdapter(adapter);

        binding.spinnerFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFood = (Food) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedFood = null;
            }
        });
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, adminControlsActivity.class);
    }
}
