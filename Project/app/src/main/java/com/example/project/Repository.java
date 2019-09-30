package com.example.project;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {
    private UserDAO userDAO;
    private WeatherDAO weatherDAO;

    private LiveData<List<UserDBEntity>> userAllWordsTemp;
    private LiveData<List<WeatherDBEntity>> weatherAllWordsTemp;

    Repository(Application application) {
        RoomDB roomDB = RoomDB.getDatabase(application);

        userDAO = roomDB.userDAO();
        weatherDAO = roomDB.weatherDAO();

        userAllWordsTemp = userDAO.getAllWords();
        weatherAllWordsTemp = weatherDAO.getAllWords();

        //TODO Finish DAO interactions - wrappers, etc.

    }
        LiveData<List<UserDBEntity>> getAllWords() {
            return userAllWordsTemp;
        }

        public void insert (UserDBEntity word) {
            new insertAsyncTask(userDAO).execute(word);
        }

        private static class insertAsyncTask extends AsyncTask<UserDBEntity, Void, Void> {

            private UserDAO mAsyncTaskDao;

            insertAsyncTask(UserDAO dao) {
                mAsyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(UserDBEntity... userDBEntities) {
                mAsyncTaskDao.insert(userDBEntities[0]);
                return null;
            }
        }

}
