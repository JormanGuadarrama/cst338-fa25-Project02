package com.example.project02.Database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project02.Database.Entities.User;
import com.example.project02.Database.Entities.Pantry;
import com.example.project02.Database.typeConverters.LocalDateTypeConverter;
import com.example.project02.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {User.class, Pantry.class}, version = 3, exportSchema = false)
public abstract class PantryManagerDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "PantryManagerDatabase";

    public static final String PANTRY_TABLE = "pantryTable";
    public static final String USER_TABLE = "userTable";

    private static volatile PantryManagerDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static PantryManagerDatabase getDatabase(final Context context) {
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
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.UserDAO();
                dao.deleteAll();
                User admin = new User("admin1", "admin1");
                admin.setAdmin(true);
                dao.insert(admin);

                User testUser1 = new User("testuser1", "testuser1");
                dao.insert(testUser1);
            });
        }
    };

    public abstract PantryDAO pantryDAO();

    public abstract UserDAO UserDAO();
}
