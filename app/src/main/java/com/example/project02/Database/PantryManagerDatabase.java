package com.example.project02.Database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project02.Database.Entities.Food;
import com.example.project02.Database.Entities.User;
import com.example.project02.Database.Entities.PantryItem;
import com.example.project02.Database.typeConverters.LocalDateTypeConverter;
import com.example.project02.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {User.class, PantryItem.class, Food.class}, version = 9, exportSchema = false)
public abstract class PantryManagerDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "PantryManagerDatabase";

    public static final String PANTRY_TABLE = "pantryTable";
    public static final String USER_TABLE = "userTable";
    public static final String FOODS_TABLE = "foodsTable";

    private static volatile PantryManagerDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static PantryManagerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PantryManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        PantryManagerDatabase.class,
                        DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(addDefaultValues)
                        .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            
            long currentTimestamp = System.currentTimeMillis();

            // 1. Insert Foods
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Apple', 'Fruit', 'Red and juicy')");
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Banana', 'Fruit', 'Sweet yellow fruit')");
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Strawberries', 'Fruit', 'Bright red berries')");

            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Broccoli', 'Vegetable', 'Green cruciferous vegetable')");
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Carrot', 'Vegetable', 'Orange root vegetable')");
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Spinach', 'Vegetable', 'Leafy green rich in iron')");

            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Rice', 'Grain', 'Long-grain white rice')");
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Whole Wheat Bread', 'Grain', 'Whole-grain loaf')");
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Pasta', 'Grain', 'Dry spaghetti noodles')");

            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Chicken Breast', 'Protein', 'Lean white meat protein')");
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Eggs', 'Protein', 'Large grade A eggs')");
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Black Beans', 'Protein', 'High-protein legumes')");

            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Milk', 'Dairy', 'Whole milk 1 gallon')");
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Yogurt', 'Dairy', 'Low-fat vanilla yogurt')");
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Cheddar Cheese', 'Dairy', 'Sharp aged cheese')");

            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Olive Oil', 'Fats/Oils', 'Extra virgin olive oil')");
            db.execSQL("INSERT INTO " + FOODS_TABLE + " (name, family, description) VALUES ('Avocado', 'Fats/Oils', 'Healthy fat-rich fruit')");

            // Insert Default Users
            db.execSQL("INSERT INTO " + USER_TABLE + " (username, password, isAdmin) VALUES ('admin1', 'admin1', 1)");
            db.execSQL("INSERT INTO " + USER_TABLE + " (username, password, isAdmin) VALUES ('testuser1', 'testuser1', 0)");

            
            // Give admin1 some items (Apple, Milk, Rice)
            db.execSQL("INSERT INTO " + PANTRY_TABLE + " (userId, foodId, quantity, dateCreated) " +
                    "VALUES (1, 1, 1, " + currentTimestamp + ")");
            
             db.execSQL("INSERT INTO " + PANTRY_TABLE + " (userId, foodId, quantity, dateCreated) SELECT " +
                    "(SELECT id FROM " + USER_TABLE + " WHERE username = 'admin1'), " +
                    "(SELECT id FROM " + FOODS_TABLE + " WHERE name = 'Milk'), " +
                    "1, " +
                    currentTimestamp);
             
             db.execSQL("INSERT INTO " + PANTRY_TABLE + " (userId, foodId, quantity, dateCreated) SELECT " +
                    "(SELECT id FROM " + USER_TABLE + " WHERE username = 'admin1'), " +
                    "(SELECT id FROM " + FOODS_TABLE + " WHERE name = 'Rice'), " +
                    "1, " +
                    currentTimestamp);

            // Give testuser1 some items (Banana, Chicken Breast)
            db.execSQL("INSERT INTO " + PANTRY_TABLE + " (userId, foodId, quantity, dateCreated) SELECT " +
                    "(SELECT id FROM " + USER_TABLE + " WHERE username = 'testuser1'), " +
                    "(SELECT id FROM " + FOODS_TABLE + " WHERE name = 'Banana'), " +
                    "1, " +
                    currentTimestamp);

            db.execSQL("INSERT INTO " + PANTRY_TABLE + " (userId, foodId, quantity, dateCreated) SELECT " +
                    "(SELECT id FROM " + USER_TABLE + " WHERE username = 'testuser1'), " +
                    "(SELECT id FROM " + FOODS_TABLE + " WHERE name = 'Chicken Breast'), " +
                    "1, " +
                    currentTimestamp);
        }
    };

    public abstract PantryDAO pantryDAO();

    public abstract UserDAO userDAO();

    public abstract FoodDAO foodDAO();
}
