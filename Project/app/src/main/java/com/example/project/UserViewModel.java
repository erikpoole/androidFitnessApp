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



    LiveData<List<UserDBEntity>> getAllUserData() {
        return allUserData;
    }

}
