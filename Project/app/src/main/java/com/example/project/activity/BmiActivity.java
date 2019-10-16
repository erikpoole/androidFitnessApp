package com.example.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;

import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.Repository;
import com.example.project.UserDBEntity;
import com.example.project.UserViewModel;
import com.example.project.activity.bio.BioEditActivity;
import com.google.android.material.navigation.NavigationView;

public class BmiActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private TextView bmiTextView;
    private String height;
    private Float heightValue;
    private Integer weight;
    private int range;
    private ActionBarDrawerToggle toggle;
    private boolean isDrawerFixed;
    private Boolean darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);

        // Handle navigation drawer
        isDrawerFixed = getResources().getBoolean(R.bool.isDrawerFixed);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("BMI");
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

        bmiTextView = findViewById(R.id.bmi_tv);
        userViewModel = new UserViewModel(this.getApplication());

        userViewModel.getHeight().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String mHeight) {
                if (mHeight != null) {
                    height = mHeight;
                    float feet = Float.parseFloat(height.split("'")[0]);
                    float inches = Float.parseFloat(height.split("'")[1].split("\"")[0]);
                    heightValue = (feet * 12) + inches; // 1 foot / 12 inches
                    displayBMI(heightValue, weight);
                }
            }
        });

        userViewModel.getWeight().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer mWeight) {
                if (mWeight != null) {
                    weight = mWeight;
                    displayBMI(heightValue, weight);
                }
            }
        });


        if (getIntent().getExtras() != null) {
            darkMode = getIntent().getExtras().getBoolean("darkMode");
        } else {
            darkMode = false;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        //Dark mode!
        userViewModel.isInDarkMode().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean mDarkMode) {
                if (mDarkMode != null) {
                    if (darkMode != mDarkMode) {
                        darkMode = mDarkMode;
                        if (darkMode) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        }
                        Intent intent = getIntent();
                        intent.putExtra("darkMode", darkMode);

                        finish();
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }
            }
        });

        displayBMI(heightValue, weight);

        SeekBar seekBar = findViewById(R.id.seek_bar);
        range = 200;
        seekBar.setMax(range);
        seekBar.setProgress(range / 2);
        final TextView seekTextView = findViewById(R.id.seek_bar_value);

        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

            Integer adjustedWeight;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustedWeight = weight;
                int change = progress - (range / 2);
                adjustedWeight += change;
                String difference = change + " lbs";
                if (change > 0) {
                    difference = "+" + difference;
                }
                seekTextView.setText(difference);
                displayBMI(heightValue, adjustedWeight);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // called after the user finishes moving the SeekBar
            }
        };

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    public void displayBMI(Float currentHeight, Integer currentWeight) {
        if (currentHeight != null && currentWeight != null) {
            float currentBMI = (currentWeight / (currentHeight * currentHeight)) * 703;
            if (currentBMI < 18.5 || currentBMI > 25.0) {
                bmiTextView.setTextColor(Color.RED);
            } else {
                bmiTextView.setTextColor(Color.parseColor("#29A100"));
            }
            bmiTextView.setText(String.format(java.util.Locale.US, "%.1f", currentBMI));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AssetHandlers.loadProfileImage(this, menu, userViewModel);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.night_mode:
                if (!darkMode) {
                    userViewModel.updateDarkMode(true);
                } else {
                    userViewModel.updateDarkMode(false);
                }
                return true;
            case R.id.edit_profile:
                Intent bioEdit = new Intent(this, BioEditActivity.class);
                startActivity(bioEdit);
                return true;
            case R.id.logout:
                userViewModel.logout();
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


}
