package com.example.project;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {

    private UserDAO userDAO;
    private WeatherDAO weatherDAO;

    private LiveData<List<UserDBEntity>> allUserData;
    private LiveData<List<WeatherDBEntity>> allWeatherData;

    Repository(Application application) {
        RoomDB roomDB = RoomDB.getDatabase(application);

        userDAO = roomDB.userDAO();
        weatherDAO = roomDB.weatherDAO();

        allUserData = userDAO.getAllUserData();
        allWeatherData = weatherDAO.getAllWeatherData();
    }

    LiveData<List<UserDBEntity>> getAllUserData() {
        return allUserData;
    }

    LiveData<List<WeatherDBEntity>> getAllWeatherData() {
        return allWeatherData;
    }

    WeatherDBEntity getWeatherForLocation(String location) {
        return weatherDAO.getWeatherForLocation(location);
    }

    public void insert(WeatherDBEntity weatherDbEntity) {
        new insertWeatherAsyncTask(weatherDAO).execute(weatherDbEntity);
    }

    public void insert(UserDBEntity userDBEntity) {
        new insertUserAsyncTask(userDAO).execute(userDBEntity);
    }

    String getName() {
        return userDAO.getName();
    }

    LiveData<String> getAge() {
        return userDAO.getAge();
    }

    LiveData<String> getSex() {
        return userDAO.getSex();
    }

    LiveData<String> getImgPath() {
        return userDAO.getImgPath();
    }

    LiveData<Integer> getGoal() {
        return userDAO.getGoal();
    }

    LiveData<Integer> getActiveState() {
        return userDAO.getActiveState();
    }

    LiveData<Boolean> hasUserLoggedIn() {
        return userDAO.hasUserLoggedIn();
    }

    LiveData<Boolean> isInDarkMode() {
        return userDAO.isInDarkMode();
    }

    void updateSex(String newSex) {
        userDAO.updateSex(newSex);
    }

    void updateImgPath(String newPath) {
        userDAO.updateImgPath(newPath);
    }

    void updateGoal(int newGoal) {
        userDAO.updateGoal(newGoal);
    }

    void updateActiveState(int newActiveState) {
        userDAO.updateActiveState(newActiveState);
    }

    void updateLogin(boolean status) {
        userDAO.updateLogin(status);
    }

    void updateDarkMode(boolean isInDarkMode) {
        userDAO.updateDarkMode(isInDarkMode);
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
