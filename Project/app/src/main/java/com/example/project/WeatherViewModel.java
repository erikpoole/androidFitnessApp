package com.example.project;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {

    private Repository repository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<WeatherDBEntity> getWeather() {
        return repository.getWeather();
    }

}
