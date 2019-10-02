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

    @Query("SELECT * FROM UserDBEntity")
    LiveData<List<UserDBEntity>> getAllUserData();

    @Query("SELECT full_name FROM UserDBEntity WHERE isLoggedIn = 1")
    String getName();

    @Query("SELECT age FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<String> getAge();

    @Query("SELECT sex FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<String> getSex();

    @Query("SELECT imgPath FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<String> getImgPath();

    @Query("SELECT goal FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<Integer> getGoal();

    @Query("SELECT activeState FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<Integer> getActiveState();

    @Query("SELECT isLoggedIn FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<Boolean> hasUserLoggedIn();

    @Query("SELECT isInDarkMode FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<Boolean> isInDarkMode();

    @Query("UPDATE UserDBEntity SET sex = :newSex WHERE isLoggedIn = 1")
    void updateSex(String newSex);

    @Query("UPDATE UserDBEntity SET imgPath = :newPath WHERE isLoggedIn = 1")
    void updateImgPath(String newPath);

    @Query("UPDATE UserDBEntity SET goal = :newGoal WHERE isLoggedIn = 1")
    void updateGoal(int newGoal);

    @Query("UPDATE UserDBEntity SET activeState = :newActiveState WHERE isLoggedIn = 1")
    void updateActiveState(int newActiveState);

    @Query("UPDATE UserDBEntity SET isLoggedIn = :status WHERE isLoggedIn = 1")
    void updateLogin(boolean status);

    @Query("UPDATE UserDBEntity SET activeState = :isInDarkMode WHERE isLoggedIn = 1")
    void updateDarkMode(boolean isInDarkMode);
}