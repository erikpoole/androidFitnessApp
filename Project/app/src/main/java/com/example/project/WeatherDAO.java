package com.example.project;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeatherDBEntity weatherData);

    @Query("SELECT * FROM Weather LIMIT 1")
    LiveData<WeatherDBEntity> getWeather();

}