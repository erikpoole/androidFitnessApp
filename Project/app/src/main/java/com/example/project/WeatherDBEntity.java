package com.example.project;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "Weather")
public class WeatherDBEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private UUID id;

    @ColumnInfo(name = "weatherJson")
    private String weatherJson;

    public WeatherDBEntity(String mWeatherJson) {
        id = UUID.randomUUID();
        weatherJson = mWeatherJson;
    }

}
