package com.example.project;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project.UserDBEntity;

import java.util.List;

@Dao
public interface WeatherDAO {

    @Insert
    void insert(WeatherDBEntity weatherData);

    @Query("DELETE FROM Weather")
    void deleteAll();

    @Query("SELECT * from Weather")
    LiveData<List<WeatherDBEntity>> getAllWeatherData();
}