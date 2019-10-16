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

    @Query("SELECT fullName FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<String> getName();

    @Query("SELECT age FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<String> getAge();

    @Query("SELECT sex FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<String> getSex();

    @Query("SELECT imgPath FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<String> getImgPath();

    @Query("SELECT height FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<String> getHeight();

    @Query("SELECT weight FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<Integer> getWeight();

    @Query("SELECT goal FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<Integer> getGoal();

    @Query("SELECT activeState FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<Integer> getActiveState();

    @Query("SELECT isLoggedIn FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<Boolean> hasUserLoggedIn();

    @Query("SELECT isInDarkMode FROM UserDBEntity WHERE isLoggedIn = 1")
    LiveData<Boolean> isInDarkMode();

    @Query("UPDATE UserDBEntity SET height = :newHeight WHERE isLoggedIn = 1")
    void updateHeight(String newHeight);

    @Query("UPDATE UserDBEntity SET weight = :newWeight WHERE isLoggedIn = 1")
    void updateWeight(int newWeight);

    @Query("UPDATE UserDBEntity SET sex = :newSex WHERE isLoggedIn = 1")
    void updateSex(String newSex);

    @Query("UPDATE UserDBEntity SET imgPath = :newPath WHERE isLoggedIn = 1")
    void updateImgPath(String newPath);

    @Query("UPDATE UserDBEntity SET goal = :newGoal WHERE isLoggedIn = 1")
    void updateGoal(int newGoal);

    @Query("UPDATE UserDBEntity SET activeState = :newActiveState WHERE isLoggedIn = 1")
    void updateActiveState(int newActiveState);

    @Query("UPDATE UserDBEntity SET isLoggedIn = :status WHERE isLoggedIn = 1")
    void logout(boolean status);

    @Query("SELECT id FROM UserDBEntity WHERE fullName = :name AND password = :password")
    LiveData<Integer> checkUser(String name, String password);

    @Query("UPDATE UserDBEntity SET isLoggedIn = 1 WHERE id = :id")
    void login(int id);

    @Query("UPDATE UserDBEntity SET isInDarkMode = :isInDarkMode WHERE isLoggedIn = 1")
    void updateDarkMode(boolean isInDarkMode);
}