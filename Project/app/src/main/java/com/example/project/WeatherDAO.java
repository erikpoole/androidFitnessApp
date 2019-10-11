package com.example.project;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeatherDBEntity weatherData);

    @Query("DELETE FROM Weather")
    void deleteAll();

    @Query("SELECT * FROM Weather")
    LiveData<List<WeatherDBEntity>> getAllWeatherData();

//    @Query("SELECT * FROM Weather WHERE location = :location LIMIT 1")
    @Query("SELECT * FROM Weather LIMIT 1")
    LiveData<WeatherDBEntity> getWeatherForLocation();

}