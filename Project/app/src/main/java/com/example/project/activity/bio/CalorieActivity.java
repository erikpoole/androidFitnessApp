package com.example.project.activity.bio;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.database.UserProfile;

public class CalorieActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    //1200 men, 1000 women
    private final int MALE_CALORIES = 2500;
    private final int MALE_ACTIVITY_MODIFIER = 500;
    private final int MALE_MIN_CALORIES = 1200;

    private final int FEMALE_CALORIES = 2000;
    private final int FEMALE_ACTIVITY_MODIFIER = 400;
    private final int FEMALE_MIN_CALORIES = 1000;

    private final int NONBINARY_CALORIES = (MALE_CALORIES + FEMALE_CALORIES) / 2;
    private final int NONBINARY_ACTIVITY_MODIFER = (MALE_ACTIVITY_MODIFIER + FEMALE_ACTIVITY_MODIFIER) / 2;
    private final int NONBINARY_MIN_CALORIES = (MALE_MIN_CALORIES + FEMALE_MIN_CALORIES) / 2;

    private final int CALORIES_PER_POUND = 500;

    private UserProfile user;

    private TextView calorieText;
    private TextView calorieWarningText;
    private TextView goalText;
    private TextView activityText;
    private SeekBar goalSeekBar;
    private SeekBar activitySeekBar;
    private Button resetButton;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

        user = new UserProfile(getApplicationContext());

        calorieText = findViewById(R.id.calorieText);
        calorieWarningText = findViewById(R.id.calorieWarningText);
        goalText = findViewById(R.id.goalValueText);
        activityText = findViewById(R.id.activityValueText);
        goalSeekBar = findViewById(R.id.goalSeekBar);
        activitySeekBar = findViewById(R.id.activitySeekBar);
        resetButton = findViewById(R.id.resetButton);
        saveButton = findViewById(R.id.saveButton);

        setDefaultValues();

        goalSeekBar.setOnSeekBarChangeListener(this);
        activitySeekBar.setOnSeekBarChangeListener(this);
        resetButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private void setDefaultValues() {
        calorieText.setText(calculateCalories());
        goalText.setText(getGoalText(user.getGoal()));
        activityText.setText(getActivityText(user.getActiveState()));
        goalSeekBar.setProgress(user.getGoal() + 2);
        activitySeekBar.setProgress(user.getActiveState() + 1);
    }


    private String calculateCalories() {
        String sex = user.getSex();
        int calories;
        switch (sex) {
            case "Male":
                calories =
                        MALE_CALORIES +
                        user.getGoal() * CALORIES_PER_POUND +
                        user.getActiveState() * MALE_ACTIVITY_MODIFIER;
                if (calories < MALE_MIN_CALORIES) {
                    calorieWarningText.setText("You are below your recommended minimum calorie limit!");
                } else {
                    calorieWarningText.setText("");
                }
                return calories + "\nCalories";
            case "Female:":
                calories =
                        FEMALE_CALORIES +
                        user.getGoal() * CALORIES_PER_POUND +
                        user.getActiveState() * FEMALE_ACTIVITY_MODIFIER;
                if (calories < FEMALE_MIN_CALORIES) {
                    calorieWarningText.setText("You are below your recommended minimum calorie limit!");
                } else {
                    calorieWarningText.setText("");
                }
                return calories + "\nCalories";
            default:
                calories =
                        NONBINARY_CALORIES +
                         user.getGoal() * CALORIES_PER_POUND +
                         user.getActiveState() * NONBINARY_ACTIVITY_MODIFER;
                if (calories < NONBINARY_ACTIVITY_MODIFER) {
                    calorieWarningText.setText("You are below your recommended minimum calorie limit!");
                } else {
                    calorieWarningText.setText("");
                }
                return calories + "\nCalories";
        }
    }

    private String getGoalText(int value) {
        if (value < 0) {
            return "Lose " + (value * - 1) + " Pounds";
        } else if (value > 0) {
            return "Gain " + value + " Pounds";
        } else {
            return "Maintain Weight";
        }
    }

    private String getActivityText(int value) {
        switch (value) {
            case -1:
                return "Low Activity";
            case 1:
                return "High Activity";
            default:
                return "Moderate Activity";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                user.update();
                break;
            case R.id.resetButton:
                user = new UserProfile(getApplicationContext());
                setDefaultValues();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.goalSeekBar:
                int pounds = progress - 2;
                user.setGoal(pounds);
                calorieText.setText(calculateCalories());
                goalText.setText(getGoalText(pounds));
                break;
            case R.id.activitySeekBar:
                int activityState = progress - 1;
                user.setActiveState(activityState);
                calorieText.setText(calculateCalories());
                activityText.setText(getActivityText(activityState));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        return;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        return;
    }
}
