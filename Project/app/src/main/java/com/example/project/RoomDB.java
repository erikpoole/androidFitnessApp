package com.example.project;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//        WeatherDAO
//        WeatherEntity (Annotations)
//        UserDAO
//        UserEntity (Annotations)


@Database(entities = {WeatherDBEntity.class, UserDBEntity.class})
public abstract class RoomDB extends RoomDatabase {

    //TODO add WeatherDAO and UserDAO
    private static volatile RoomDB INSTANCE;

    static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RoomDB.class,
                            "roomDB")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
