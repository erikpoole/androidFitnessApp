package com.example.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import com.android.volley.Response;
import com.example.project.activity.Weather.WeatherFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Repository {
    final String DARK_SKY_API_URL = "https://api.darksky.net/forecast/";
    final String DARK_SKY_API_KEY = "3ed44328d0b34c77cc6a6ee7ce334c3c";

    private Context context;

    private UserDAO userDAO;
    private WeatherDAO weatherDAO;

    private LiveData<List<UserDBEntity>> allUserData;
    private LiveData<List<WeatherDBEntity>> allWeatherData;

    Repository(Application application) {
        RoomDB roomDB = RoomDB.getDatabase(application);
        context = application.getApplicationContext();

        userDAO = roomDB.userDAO();
        weatherDAO = roomDB.weatherDAO();

        allUserData = userDAO.getAllUserData();
        allWeatherData = weatherDAO.getAllWeatherData();

        //TODO Maybe should make polling in the future
        updateWeatherData();
    }

    public void updateWeatherData() {
        Log.d("DATA:", "updateWeatherDataCalled");
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (isNetworkAvailable() && hasWeatherPermissions()) {
            @SuppressLint("MissingPermission")
            Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            getWeather(location);
        }
    }

    private boolean hasWeatherPermissions() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getWeather(Location location) {
        //handles first time access if getLastKnownLocation doesn't exist
        Log.d("DATA:", "getWeatherCalled");
        if (location == null) {
            Log.d("DATA:", "getWeather: default value used...");
            location = new Location("");
            location.setLongitude(-111.89);
            location.setLatitude(40.76);
        }

        String latitude = Double.toString(location.getLatitude());
        String longitude = Double.toString(location.getLongitude());
        String url = DARK_SKY_API_URL +
                DARK_SKY_API_KEY +
                "/" +
                latitude +
                "," +
                longitude +
                "?exclude=minutely,hourly,alerts,flags";

        Requests.makeStringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        WeatherDBEntity weatherDBEntity = new WeatherDBEntity("poopville", response);
                        new insertWeatherAsyncTask(weatherDAO).execute(weatherDBEntity);
                    }
                },
                context);
    }


    LiveData<List<UserDBEntity>> getAllUserData() {
        return allUserData;
    }

    LiveData<List<WeatherDBEntity>> getAllWeatherData() {
        return allWeatherData;
    }

    LiveData<WeatherDBEntity> getWeatherForLocation() {
        Log.d("DATA:", "location: ");

        return weatherDAO.getWeatherForLocation();
    }

    public void insert(WeatherDBEntity weatherDbEntity) {
        new insertWeatherAsyncTask(weatherDAO).execute(weatherDbEntity);
    }

    public void insert(UserDBEntity word) {
        new insertUserAsyncTask(userDAO).execute(word);
    }

    private static class insertUserAsyncTask extends AsyncTask<UserDBEntity, Void, Void> {

        private UserDAO mAsyncTaskDao;

        insertUserAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserDBEntity... userDBEntities) {
            mAsyncTaskDao.insert(userDBEntities[0]);
            return null;
        }
    }

    private static class insertWeatherAsyncTask extends AsyncTask<WeatherDBEntity, Void, Void> {

        private WeatherDAO mAsyncTaskDao;

        insertWeatherAsyncTask(WeatherDAO mWeatherDao) {
            mAsyncTaskDao = mWeatherDao;
        }

        @Override
        protected Void doInBackground(WeatherDBEntity... weatherDBEntities) {
            mAsyncTaskDao.insert(weatherDBEntities[0]);
            return null;
        }
    }

}
