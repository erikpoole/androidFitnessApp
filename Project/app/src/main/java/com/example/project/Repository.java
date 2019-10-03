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

    void deleteAllUserData() {
        userDAO.deleteAll();
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

    public void updateSex(String newSex) {
        userDAO.updateSex(newSex);
    }

    public void updateImgPath(String newPath) {
        userDAO.updateImgPath(newPath);
    }

    public void updateGoal(int newGoal) {
        userDAO.updateGoal(newGoal);
    }

    public void updateActiveState(int newActiveState) {
        userDAO.updateActiveState(newActiveState);
    }

    public void updateDarkMode(boolean isInDarkMode) {
        userDAO.updateDarkMode(isInDarkMode);
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

    private static class loginAsyncTask extends AsyncTask<Integer, Void, Void> {

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
