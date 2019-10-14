package com.example.project;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {

    private Repository repository;

    private MutableLiveData<String> temperature;
    private MutableLiveData<String> humidity;
    private MutableLiveData<String> windSpeed;
    private MutableLiveData<String> summary;
    private MutableLiveData<JSONArray> weatherForcast;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);

        temperature = new MutableLiveData<>();
        humidity = new MutableLiveData<>();
        windSpeed = new MutableLiveData<>();
        summary = new MutableLiveData<>();

        weatherForcast = new MutableLiveData<>();

        getWeather().observeForever(new Observer<WeatherDBEntity>() {
            @Override
            public void onChanged(WeatherDBEntity weatherDBEntity) {
                if (weatherDBEntity != null) {
                    try {
                        JSONObject json = new JSONObject(weatherDBEntity.getWeatherJson());
                        JSONObject current = json.getJSONObject("currently");

                        temperature.setValue(roundStringWithMultiplier(current.getString("temperature"), 1));
                        humidity.setValue(roundStringWithMultiplier(current.getString("humidity"), 100));
                        windSpeed.setValue(roundStringWithMultiplier(current.getString("windSpeed"), 1));
                        summary.setValue(json.getJSONObject("daily")
                                .getJSONArray("data")
                                .getJSONObject(0)
                                .getString("summary"));

                        weatherForcast.setValue(json.getJSONObject("daily").getJSONArray("data"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }

                }
            }
        });
    }

    private String roundStringWithMultiplier(String input, int multiplier) {
        return String.valueOf(Math.round(Double.parseDouble(input) * multiplier));
    }

    public LiveData<WeatherDBEntity> getWeather() {
        return repository.getWeather();
    }

    public MutableLiveData<String> getTemperature() {
        return temperature;
    }

    public MutableLiveData<String> getHumidity() {
        return humidity;
    }

    public MutableLiveData<String> getWindSpeed() {
        return windSpeed;
    }

    public MutableLiveData<String> getSummary() {
        return summary;
    }

    public MutableLiveData<JSONArray> getWeatherForcast() {
        return weatherForcast;
    }


}
