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
    void insert(WeatherDBEntity word);

    @Query("DELETE FROM WeatherDBEntity")
    void deleteAll();

    @Query("SELECT * from WeatherDBEntity")
    LiveData<List<WeatherDBEntity>> getAllWords();
}