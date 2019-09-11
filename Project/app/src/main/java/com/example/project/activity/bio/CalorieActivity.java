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

    private final int MALE_CALORIES = 2500;
    private final int MALE_ACTIVITY_MODIFIER = 500;

    private final int FEMALE_CALORIES = 2000;
    private final int FEMALE_ACTIVITY_MODIFIER = 400;

    private final int NONBINARY_CALORIES = (MALE_CALORIES + FEMALE_CALORIES) / 2;
    private final int NONBINARY_ACTIVITY_MODIFER = (MALE_ACTIVITY_MODIFIER + FEMALE_ACTIVITY_MODIFIER) / 2;

    private final int CALORIES_PER_POUND = 500;

    private UserProfile user;

    private TextView caloriesText;
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

        caloriesText = findViewById(R.id.calorieText);
        caloriesText.setText(calculateCalories());
        goalText = findViewById(R.id.goalValueText);
        goalText.setText(user.getGoal() + "lbs");
        activityText = findViewById(R.id.activityValueText);
        activityText.setText(user.getActiveState());

        goalSeekBar = findViewById(R.id.goalSeekBar);
        goalSeekBar.setOnSeekBarChangeListener(this);

        activitySeekBar = findViewById(R.id.activitySeekBar);
        activitySeekBar.setOnSeekBarChangeListener(this);

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
    }



    private String calculateCalories() {
        String sex = user.getSex();
        switch (sex) {
            case "male":
                return (MALE_CALORIES +
                        user.getGoal() * CALORIES_PER_POUND +
                        getActivityLevelInt() * MALE_ACTIVITY_MODIFIER);
            case "female:":
                return (FEMALE_CALORIES +
                        user.getGoal() * CALORIES_PER_POUND +
                        getActivityLevelInt() * FEMALE_ACTIVITY_MODIFIER);
            default:
                return (NONBINARY_CALORIES +
                        user.getGoal() * CALORIES_PER_POUND +
                        getActivityLevelInt() * NONBINARY_ACTIVITY_MODIFER);
        }
    }

    private String getActivityText(int value) {
        switch (value) {
            case 0:
                return "Low Activity";
            case 1:
                return "Moderate Activity";
            case 2:
                return "High Activity";
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.goalSeekBar:
                int pounds = progress - 2;
                if (pounds < 0) {
                    goalText.setText("Lose " + pounds + " Pounds");
                } else if (pounds > 0) {
                    goalText.setText("Gain " + pounds + " Pounds");
                } else {
                    goalText.setText("Maintain Weight");
                }
                break;
            case R.id.activitySeekBar:
                activityText.setText(getActivityText(progress));
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
