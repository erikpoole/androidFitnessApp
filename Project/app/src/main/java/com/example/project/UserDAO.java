package com.example.project;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insert(UserDBEntity word);

    @Query("DELETE FROM UserDBEntity")
    void deleteAll();

    @Query("SELECT * from UserDBEntity")
    LiveData<List<UserDBEntity>> getAllUserData();
}