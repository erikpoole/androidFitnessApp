package com.example.project;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WeatherDBEntity {

    @PrimaryKey
    @NonNull
    private String mWord;

    public WeatherDBEntity(String word) {this.mWord = word;}

    public String getWord(){return this.mWord;}
}
