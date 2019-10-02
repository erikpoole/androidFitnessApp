package com.example.project;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insert(UserDBEntity userProfile);

    @Query("DELETE FROM UserDBEntity")
    void deleteAll();

    @Query("SELECT * from UserDBEntity")
    LiveData<List<UserDBEntity>> getAllUserData();

    @Query("SELECT full_name from UserDBEntity where isLoggedIn = 1")
    String getName();

    @Query("SELECT age from UserDBEntity where isLoggedIn = 1")
    LiveData<String> getAge();

    @Query("SELECT sex from UserDBEntity where isLoggedIn = 1")
    LiveData<String> getSex();

    @Query("SELECT imgPath from UserDBEntity where isLoggedIn = 1")
    LiveData<String> getImgPath();

    @Query("SELECT goal from UserDBEntity where isLoggedIn = 1")
    LiveData<Integer> getGoal();

    @Query("SELECT activeState from UserDBEntity where isLoggedIn = 1")
    LiveData<Integer> getActiveState();

    @Query("SELECT isLoggedIn from UserDBEntity where isLoggedIn = 1")
    LiveData<Boolean> hasUserLoggedIn();

    @Query("SELECT isInDarkMode from UserDBEntity where isLoggedIn = 1")
    LiveData<Boolean> isInDarkMode();
}