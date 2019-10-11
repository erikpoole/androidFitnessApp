package com.example.project;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Weather")
public class WeatherDBEntity {

    @PrimaryKey
    @NonNull
    private int id;

    @ColumnInfo(name = "weatherJson")
    private String weatherJson;

    public WeatherDBEntity(String weatherJson) {
        /*
        ensures that there is only ever one record stored -
        which kind of defeats the point of a database
        but that's just the world we live in
        */
        this.id = 0;
        this.weatherJson = weatherJson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeatherJson() {
        return weatherJson;
    }
}
