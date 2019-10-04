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

    // WEATHER CALLS

    LiveData<List<WeatherDBEntity>> getAllWeatherData() {
        return allWeatherData;
    }

    WeatherDBEntity getWeatherForLocation(String location) {
        return weatherDAO.getWeatherForLocation(location);
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
