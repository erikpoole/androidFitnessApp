package com.example.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;

import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.UserViewModel;
import com.example.project.activity.bio.BioEditActivity;
import com.example.project.database.UserProfile;
import com.google.android.material.navigation.NavigationView;

public class CalorieActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private UserViewModel userViewModel;

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

    private Integer originalGoal;
    private Integer originalActivity;
    private String sex;
    private Integer workingGoal;
    private Integer workingActivity;

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

        calorieText = findViewById(R.id.calorieText);
        calorieWarningText = findViewById(R.id.calorieWarningText);
        goalText = findViewById(R.id.goalValueText);
        activityText = findViewById(R.id.activityValueText);
        goalSeekBar = findViewById(R.id.goalSeekBar);
        activitySeekBar = findViewById(R.id.activitySeekBar);
        resetButton = findViewById(R.id.resetButton);
        saveButton = findViewById(R.id.saveButton);

        // Handle navigation drawer
        isDrawerFixed = getResources().getBoolean(R.bool.isDrawerFixed);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Calories");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (!isDrawerFixed) {
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        }
        NavigationView nav = findViewById(R.id.nav_view);
        final Activity activity = this;
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return AssetHandlers.handleNavigationEvent(activity, item);
            }
        });

        disableButtons();

        goalSeekBar.setOnSeekBarChangeListener(this);
        activitySeekBar.setOnSeekBarChangeListener(this);
        resetButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        userViewModel = new UserViewModel(this.getApplication());

        userViewModel.getGoal().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer goal) {
                Log.d("DATA:", goal.toString());
                if (goal != null) {
                    originalGoal = goal;
                    workingGoal = goal;
                    goalText.setText(getGoalText(goal));
                    goalSeekBar.setProgress(goal + 2);

                    calorieText.setText(calculateCalories());
                }
            }
        });

        userViewModel.getActiveState().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer activity) {
                Log.d("DATA:", activity.toString());
                if (activity != null) {
                    originalActivity = activity;
                    workingActivity = activity;
                    activityText.setText(getActivityText(activity));
                    activitySeekBar.setProgress(activity + 1);

                    calorieText.setText(calculateCalories());
                }
            }
        });

        userViewModel.getSex().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String sex) {
                Log.d("DATA:", sex);
                if (sex != null) {
                    CalorieActivity.this.sex = sex;

                    calculateCalories();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AssetHandlers.loadProfileImage(this, menu, user);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.night_mode:
                if (!user.isInDarkMode()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                user.toggleDarkMode();
                user.update();
                finish();
                startActivity(getIntent());
                return true;
            case R.id.edit_profile:
                Intent bioEdit = new Intent(this, BioEditActivity.class);
                startActivity(bioEdit);
                return true;
            case R.id.logout:
                UserProfile userProfile = new UserProfile(getApplicationContext());
                userProfile.logout();
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private String calculateCalories() {
        int calories = 0;
        if (workingGoal == null || workingActivity == null || sex == null) {
            return calories + "\nCalories";
        }

        switch (sex) {
            case "Male":
                calories =
                        MALE_CALORIES +
                                workingGoal * CALORIES_PER_POUND +
                                workingActivity * MALE_ACTIVITY_MODIFIER;
                if (calories < MALE_MIN_CALORIES) {
                    calorieWarningText.setText("You are below your recommended minimum calorie limit!");
                } else {
                    calorieWarningText.setText("");
                }
                return calories + "\nCalories";
            case "Female:":
                calories =
                        FEMALE_CALORIES +
                                workingGoal * CALORIES_PER_POUND +
                                workingActivity * FEMALE_ACTIVITY_MODIFIER;
                if (calories < FEMALE_MIN_CALORIES) {
                    calorieWarningText.setText("You are below your recommended minimum calorie limit!");
                } else {
                    calorieWarningText.setText("");
                }
                return calories + "\nCalories";
            default:
                calories =
                        NONBINARY_CALORIES +
                                workingGoal * CALORIES_PER_POUND +
                                workingActivity * NONBINARY_ACTIVITY_MODIFER;
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
            return "Lose " + (value * -1) + " Pounds";
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
                userViewModel.updateGoal(workingGoal);
                userViewModel.updateActiveState(workingActivity);
                disableButtons();
                Toast.makeText(getApplicationContext(), "Goals Saved!", Toast.LENGTH_LONG).show();
                break;
            case R.id.resetButton:
                calorieText.setText(calculateCalories());
                goalText.setText(getGoalText(originalGoal));
                activityText.setText(getActivityText(originalActivity));
                goalSeekBar.setProgress(originalGoal + 2);
                activitySeekBar.setProgress(originalActivity + 1);
                disableButtons();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.goalSeekBar:
                int pounds = progress - 2;
                workingGoal = pounds;
                calorieText.setText(calculateCalories());
                goalText.setText(getGoalText(pounds));
                break;
            case R.id.activitySeekBar:
                int activityState = progress - 1;
                workingActivity = activityState;
                calorieText.setText(calculateCalories());
                activityText.setText(getActivityText(activityState));
                break;
        }
        if (originalGoal != workingGoal ||
                originalActivity != workingActivity) {
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
