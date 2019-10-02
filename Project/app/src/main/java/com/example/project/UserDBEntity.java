package com.example.project;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserDBEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "full_name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "password")
    private String mPassword;

    @NonNull
    @ColumnInfo(name = "age")
    private String mAge;

    @NonNull
    @ColumnInfo(name = "sex")
    private String mSex;

    @NonNull
    @ColumnInfo(name = "imgPath")
    private String mImgPath;

    @NonNull
    @ColumnInfo(name = "goal")
    private int mGoal;

    @NonNull
    @ColumnInfo(name = "activeState")
    private int mActiveState;

    @NonNull
    @ColumnInfo(name = "isLoggedIn")
    private boolean mIsLoggedIn;

    @NonNull
    @ColumnInfo(name = "isInDarkMode")
    private boolean mIsInDarkMode;

    public UserDBEntity(String name, String password, String age, String sex, String imgPath) {
        this.mName = name;
        this.mPassword = password;
        this.mAge = age;
        this.mSex = sex;
        this.mImgPath = imgPath;
        this.mGoal = 0;
        this.mActiveState = 0;
        this.mIsLoggedIn = true;
        this.mIsInDarkMode = false;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.mName;
    }

    public String getPassword(){
        return this.mPassword;
    }

    public String getAge(){
        return this.mAge;
    }

    public String getSex(){
        return this.mSex;
    }

    public String getImgPath(){
        return this.mImgPath;
    }

    public int getGoal() {
        return this.mGoal;
    }

    public int getActiveState(){
        return this.mActiveState;
    }

    public boolean getIsLoggedIn(){
        return this.mIsLoggedIn;
    }

    public boolean getIsInDarkMode(){
        return this.mIsInDarkMode;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setGoal(int goal) {
        this.mGoal = goal;
    }

    public void setActiveState(int activeState) {
        this.mActiveState = activeState;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.mIsLoggedIn = isLoggedIn;
    }

    public void setIsInDarkMode(boolean isInDarkMode) {
        this.mIsInDarkMode = isInDarkMode;
    }

    public static class UserViewModel {
    }
}
