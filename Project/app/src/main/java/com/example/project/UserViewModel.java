package com.example.project;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<UserDBEntity>> allUserData;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allUserData = repository.getAllUserData();
    }

    String getName() {
        return repository.getName();
    }

    LiveData<String> getAge() {
        return repository.getAge();
    }

    LiveData<String> getSex() {
        return repository.getSex();
    }

    LiveData<String> getImgPath() {
        return repository.getImgPath();
    }

    LiveData<Integer> getGoal() {
        return repository.getGoal();
    }

    LiveData<Integer> getActiveState() {
        return repository.getActiveState();
    }

    LiveData<Boolean> hasUserLoggedIn() {
        return repository.hasUserLoggedIn();
    }

    LiveData<Boolean> isInDarkMode() {
        return repository.isInDarkMode();
    }

    LiveData<List<UserDBEntity>> getAllUserData() {
        return allUserData;
    }
}
