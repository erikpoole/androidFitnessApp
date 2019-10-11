package com.example.project;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WeatherDBEntity.class, UserDBEntity.class}, version = 2, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    private static volatile RoomDB INSTANCE;

    public abstract UserDAO userDAO();
    public abstract WeatherDAO weatherDAO();

    static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    RoomDB.class,
                    "roomDB")
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
