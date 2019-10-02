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
    @ColumnInfo(name = "location")
    @NonNull
    private String location;

    @ColumnInfo(name = "weatherJson")
    private String weatherJson;

    public WeatherDBEntity(String location, String weatherJson) {
        this.location = location;
        this.weatherJson = weatherJson;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeatherJson() {
        return weatherJson;
    }

    public void setWeatherJson(String weatherJson) {
        this.weatherJson = weatherJson;
    }
}
