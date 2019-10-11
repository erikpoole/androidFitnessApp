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

    // USER PROFILE CALLS
    LiveData<List<UserDBEntity>> getAllUserData() {
        return allUserData;
    }

    void deleteAllUserData() {
        userDAO.deleteAll();
    }

    public void insert(UserDBEntity userDBEntity) {
        new insertUserAsyncTask(userDAO).execute(userDBEntity);
    }

    // GETTERS
    LiveData<String> getName() {
        return userDAO.getName();
    }

    LiveData<String> getAge() {
        return userDAO.getAge();
    }

    LiveData<String> getSex() {
        return userDAO.getSex();
    }

    public LiveData<String> getHeight() {
        return userDAO.getHeight();
    }

    public LiveData<Integer> getWeight() {
        return userDAO.getWeight();
    }

    public LiveData<String> getImgPath() {
        return userDAO.getImgPath();
    }

    public LiveData<Integer> getGoal() {
        return userDAO.getGoal();
    }

    public LiveData<Integer> getActiveState() {
        return userDAO.getActiveState();
    }

    public LiveData<Boolean> hasUserLoggedIn() {
        return userDAO.hasUserLoggedIn();
    }

    public LiveData<Boolean> isInDarkMode() {
        return userDAO.isInDarkMode();
    }


    // SETTERS
    public void updateHeight(String height) {
        new updateHeightAsyncTask(userDAO).execute(height);
    }

    public void updateWeight(int weight) {
        new updateWeightAsyncTask(userDAO).execute(weight);
    }

    public void updateSex(String newSex) {
        new updateSexAsyncTask(userDAO).execute(newSex);
    }

    public void updateImgPath(String newPath) {
        new updateImgPathAsyncTask(userDAO).execute(newPath);
    }

    public void updateGoal(int newGoal) {
        new updateGoalAsyncTask(userDAO).execute(newGoal);
    }

    public void updateActiveState(int newActiveState) {
        new updateActiveStateAsyncTask(userDAO).execute(newActiveState);
    }

    public void updateDarkMode(boolean isInDarkMode) {
        new updateDarkModeAsyncTask(userDAO).execute(isInDarkMode);
    }

    public LiveData<Integer> checkUser(String name, String password) {
        return userDAO.checkUser(name, password);
    }

    public void login(int id) {
        new loginAsyncTask(userDAO).execute(id);
    }

    public void logout() {
        new logoutAsyncTask(userDAO).execute(false);
    }


    // Async wrappers for SETTERS
    private static class insertUserAsyncTask extends android.os.AsyncTask<UserDBEntity, Void, Void> {

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

    private static class logoutAsyncTask extends AsyncTask<Boolean, Void, Void> {

        private UserDAO mAsyncTaskDao;

        logoutAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Boolean... status) {
            mAsyncTaskDao.logout(status[0]);
            return null;
        }
    }

    private static class loginAsyncTask extends android.os.AsyncTask<Integer, Void, Void> {

        private UserDAO mAsyncTaskDao;

        loginAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... id) {
            mAsyncTaskDao.login(id[0]);
            return null;
        }
    }

    private static class updateHeightAsyncTask extends AsyncTask<String, Void, Void> {

        private UserDAO mAsyncTaskDao;

        updateHeightAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... height) {
            mAsyncTaskDao.updateHeight(height[0]);
            return null;
        }
    }

    private static class updateWeightAsyncTask extends AsyncTask<Integer, Void, Void> {

        private UserDAO mAsyncTaskDao;

        updateWeightAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... weight) {
            mAsyncTaskDao.updateWeight(weight[0]);
            return null;
        }
    }

    private static class updateSexAsyncTask extends AsyncTask<String, Void, Void> {

        private UserDAO mAsyncTaskDao;

        updateSexAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... sex) {
            mAsyncTaskDao.updateSex(sex[0]);
            return null;
        }
    }

    private static class updateImgPathAsyncTask extends AsyncTask<String, Void, Void> {

        private UserDAO mAsyncTaskDao;

        updateImgPathAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... imgPath) {
            mAsyncTaskDao.updateImgPath(imgPath[0]);
            return null;
        }
    }

    private static class updateGoalAsyncTask extends AsyncTask<Integer, Void, Void> {

        private UserDAO mAsyncTaskDao;

        updateGoalAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... goal) {
            mAsyncTaskDao.updateGoal(goal[0]);
            return null;
        }
    }

    private static class updateActiveStateAsyncTask extends AsyncTask<Integer, Void, Void> {

        private UserDAO mAsyncTaskDao;

        updateActiveStateAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... activeState) {
            mAsyncTaskDao.updateActiveState(activeState[0]);
            return null;
        }
    }

    private static class updateDarkModeAsyncTask extends AsyncTask<Boolean, Void, Void> {

        private UserDAO mAsyncTaskDao;

        updateDarkModeAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Boolean... darkStatus) {
            mAsyncTaskDao.updateDarkMode(darkStatus[0]);
            return null;
        }
    }

}
