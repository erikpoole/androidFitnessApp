package com.example.project.activity.bio;

import android.os.Bundle;
import android.util.Log;
<<<<<<< HEAD
import android.view.View;
import android.widget.Switch;
=======
>>>>>>> 19b73413cad0a5e502465dee7b3ceb5f745e78ce
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.database.UserProfile;

public class CalorieActivity extends AppCompatActivity {

    private final int MALE_CALORIES = 2500;
    private final int FEMALE_CALORIES = 2000;
    private final int MALE_ACTIVITY_MODIFIER = 500;
    private final int FEMALE_ACTIVITY_MODIFIER = 400;
    private final int CALORIES_PER_POUND = 500;

    private int activityLevel;
    private int weightGoal;
    private boolean isMale;

    private UserProfile user;

    private TextView caloriesView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

        user = new UserProfile(getApplicationContext());
        activityLevel = getActivityLevelInt();
//        weightGoal = user.getGoal();
        weightGoal = 0;
        isMale = user.getSex() == "male" ? true : false;
    }

    private int calculateCalories() {
        return (isMale ? MALE_CALORIES: FEMALE_CALORIES) +
                weightGoal * CALORIES_PER_POUND +
                activityLevel * (isMale ? MALE_ACTIVITY_MODIFIER: FEMALE_ACTIVITY_MODIFIER);
    }

    //TODO what if null?
    private int getActivityLevelInt() {
        String activityLevelString = user.getActiveState();
        switch (activityLevelString) {
            case "active":
                return 1;
            case "standard":
                return 0;
            case "sedentary":
                return -1;
            default:
                return 0;
        }
    }
}
