package com.example.project.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.project.R;
import com.example.project.activity.Weather.WeatherActivity;
import com.example.project.activity.bio.BioActivity;
import com.example.project.database.UserProfile;
import com.google.android.material.navigation.NavigationView;

public class CalorieActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

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

    private int originalGoal;
    private int originalActivity;

    private TextView calorieText;
    private TextView calorieWarningText;
    private TextView goalText;
    private TextView activityText;
    private SeekBar goalSeekBar;
    private SeekBar activitySeekBar;
    private Button resetButton;
    private Button saveButton;

    private ActionBarDrawerToggle toggle;
    private boolean isDrawerFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

        user = new UserProfile(getApplicationContext());

        originalGoal = user.getGoal();
        originalActivity = user.getActiveState();

        calorieText = findViewById(R.id.calorieText);
        calorieWarningText = findViewById(R.id.calorieWarningText);
        goalText = findViewById(R.id.goalValueText);
        activityText = findViewById(R.id.activityValueText);
        goalSeekBar = findViewById(R.id.goalSeekBar);
        activitySeekBar = findViewById(R.id.activitySeekBar);
        resetButton = findViewById(R.id.resetButton);
        saveButton = findViewById(R.id.saveButton);

        // Handle navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (!isDrawerFixed) {
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        }
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return handleNavigationEvent(item);
            }
        });
        isDrawerFixed = getResources().getBoolean(R.bool.isDrawerFixed);

        setDefaultValues();
        disableButtons();

        goalSeekBar.setOnSeekBarChangeListener(this);
        activitySeekBar.setOnSeekBarChangeListener(this);
        resetButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    public boolean handleNavigationEvent(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_home:
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                return true;
            case R.id.nav_weather:
                Intent weatherPage = new Intent(this, WeatherActivity.class);
                startActivity(weatherPage);
                return true;
            case R.id.nav_hiking:
                Intent hikingPage = new Intent(this, HikingActivity.class);
                startActivity(hikingPage);
                return true;
            case R.id.nav_bio:
                Intent bioPage = new Intent(this, BioActivity.class);
                startActivity(bioPage);
                return true;
            case R.id.nav_bmi:
                Intent bmiPage = new Intent(this, BmiActivity.class);
                startActivity(bmiPage);
                return true;
            case R.id.nav_calorie:
                Intent caloriePage = new Intent(this, CalorieActivity.class);
                startActivity(caloriePage);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!isDrawerFixed) {
            toggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
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
                if (calories < NONBINARY_MIN_CALORIES) {
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

    private void enableButtons() {
        saveButton.setEnabled(true);
        resetButton.setEnabled(true);
        saveButton.setAlpha(1f);
        resetButton.setAlpha(1f);
    }

    private void disableButtons() {
        saveButton.setEnabled(false);
        resetButton.setEnabled(false);
        saveButton.setAlpha(.5f);
        resetButton.setAlpha(.5f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                user.update();
                originalGoal = user.getGoal();
                originalActivity = user.getActiveState();
                disableButtons();
                Toast.makeText(getApplicationContext(), "Goals Saved!", Toast.LENGTH_LONG).show();
                break;
            case R.id.resetButton:
                user = new UserProfile(getApplicationContext());
                setDefaultValues();
                disableButtons();
                break;
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
        if (originalGoal != goalSeekBar.getProgress() - 2 ||
                originalActivity != activitySeekBar.getProgress() - 1) {
            enableButtons();
        } else {
            disableButtons();
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
