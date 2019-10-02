package com.example.project;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "Weather")
public class WeatherDBEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private UUID id;

    @ColumnInfo(name = "weatherJson")
    private String weatherJson;

    public WeatherDBEntity(String weatherJson) {
        id = UUID.randomUUID();
        this.weatherJson = weatherJson;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID mId) {
        id = mId;
    }

    public String getWeatherJson() {
        return weatherJson;
    }

    public void setWeatherJson(String mWeatherJson) {
        weatherJson = mWeatherJson;
    }
}
