package com.example.project;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<UserDBEntity>> userAllWordsTemp;

    public ViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        userAllWordsTemp = repository.getAllUserData();

    }

    LiveData<List<UserDBEntity>> getAllUserDB() {return userAllWordsTemp;}

    public void insert(UserDBEntity userDBEntity) {repository.insert(userDBEntity);}
}
