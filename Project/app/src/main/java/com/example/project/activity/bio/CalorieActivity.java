package com.example.project.activity.bio;

import android.os.Bundle;

import com.example.project.database.UserProfile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.project.R;

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
        Log.d("DATA:", user.getActiveState());
    }

    private int calculateCalories() {
        return (isMale ? MALE_CALORIES: FEMALE_CALORIES) +
                weightGoal * CALORIES_PER_POUND +
                activityLevel * (isMale ? MALE_ACTIVITY_MODIFIER: FEMALE_ACTIVITY_MODIFIER);
    }

    private int getActivityLevelInt() {
        String activityLevelString = user.getActiveState();
//        if (get )
    }
}
