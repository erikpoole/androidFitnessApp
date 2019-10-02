package com.example.project;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<WeatherDBEntity>> allWeatherData;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allWeatherData = repository.getAllWeatherData();
    }

    LiveData<List<WeatherDBEntity>> getAllWeatherData() {
        return allWeatherData;
    }

    public void insert(WeatherDBEntity weatherDBEntity) {
        repository.insert(weatherDBEntity);
    }

    public String getMostRecentWeatherJsonForLocation() {
        return "";
    }

}
