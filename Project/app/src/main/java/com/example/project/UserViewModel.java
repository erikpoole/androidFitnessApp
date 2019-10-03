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

    public void  deleteAllUserData() {
        repository.deleteAllUserData();
    }

    public LiveData<String> getName() {
        return repository.getName();
    }

    public LiveData<String> getAge() {
        return repository.getAge();
    }

    public LiveData<String> getSex() {
        return repository.getSex();
    }

    public LiveData<String> getHeight() {
        return repository.getHeight();
    }

    public LiveData<Integer> getWeight() {
        return repository.getWeight();
    }

    public LiveData<String> getImgPath() {
        return repository.getImgPath();
    }

    public LiveData<Integer> getGoal() {
        return repository.getGoal();
    }

    public LiveData<Integer> getActiveState() {
        return repository.getActiveState();
    }

    public LiveData<Boolean> hasUserLoggedIn() {
        return repository.hasUserLoggedIn();
    }

    public LiveData<Boolean> isInDarkMode() {
        return repository.isInDarkMode();
    }

    public void updateSex(String newSex) {
        repository.updateSex(newSex);
    }

    public void updateImgPath(String newPath) {
        repository.updateImgPath(newPath);
    }

    public void updateGoal(int newGoal) {
        repository.updateGoal(newGoal);
    }

    public void updateActiveState(int newActiveState) {
        repository.updateActiveState(newActiveState);
    }

    public void logout() {
        repository.logout();
    }

    public LiveData<Integer> checkUser(String name, String password) {
        return repository.checkUser(name, password);
    }

    public void login(int id) {
        repository.login(id);
    }

    public void insert(String name, String password, String age, String sex, String height, int weight, String imgPath) {
        UserDBEntity dbEntity = new UserDBEntity(name, password, age, sex, height, weight, imgPath);
        repository.insert(dbEntity);
    }

    public LiveData<List<UserDBEntity>> getAllUserData() {
        return allUserData;
    }
}
