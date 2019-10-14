package com.example.project;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final int MALE_CALORIES = 2500;
    private final int MALE_ACTIVITY_MODIFIER = 500;

    private final int FEMALE_CALORIES = 2000;
    private final int FEMALE_ACTIVITY_MODIFIER = 400;

    private final int NONBINARY_CALORIES = (MALE_CALORIES + FEMALE_CALORIES) / 2;
    private final int NONBINARY_ACTIVITY_MODIFER = (MALE_ACTIVITY_MODIFIER + FEMALE_ACTIVITY_MODIFIER) / 2;

    private final int CALORIES_PER_POUND = 500;

    private Repository repository;
    private LiveData<List<UserDBEntity>> allUserData;

    private Integer goal;
    private Integer activity;
    private String sex;
    private MutableLiveData<Integer> calories;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allUserData = repository.getAllUserData();

        calories = new MutableLiveData<>();

        getGoal().observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer inputGoal) {
                if (inputGoal != null) {
                    goal = inputGoal;
                    calories.setValue(calculateCalories());
                }
            }
        });

        getActiveState().observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer inputActivity) {
                if (inputActivity != null) {
                    activity = inputActivity;
                    calories.setValue(calculateCalories());
                }
            }
        });

        getSex().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String inputSex) {
                if (inputSex != null) {
                    sex = inputSex;
                    calories.setValue(calculateCalories());
                }
            }
        });

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

    public void updateHeight(String newHeight) {
        repository.updateHeight(newHeight);
    }

    public void updateWeight(int newWeight) {
        repository.updateWeight(newWeight);
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

    public void updateDarkMode(boolean isInDarkMode) {
        repository.updateDarkMode(isInDarkMode);
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

    public MutableLiveData<Integer> getCalories() {
        return calories;
    }

    private Integer calculateCalories() {
        int calories = 0;
        if (goal == null || activity == null || sex == null) {
            return calories;
        }

        switch (sex) {
            case "Male":
                calories =
                        MALE_CALORIES + goal *
                                CALORIES_PER_POUND +
                                activity * MALE_ACTIVITY_MODIFIER;
                return calories;
            case "Female:":
                calories =
                        FEMALE_CALORIES +
                                goal * CALORIES_PER_POUND +
                                activity * FEMALE_ACTIVITY_MODIFIER;
                return calories;
            default:
                calories =
                        NONBINARY_CALORIES +
                                goal * CALORIES_PER_POUND +
                                activity * NONBINARY_ACTIVITY_MODIFER;
                return calories;
        }
    }

    public void setCurrentGoal(Integer goal) {
        this.goal = goal;
        calories.setValue(calculateCalories());
    }

    public void setCurrentActivity(Integer activity) {
        this.activity = activity;
        calories.setValue(calculateCalories());
    }
}



