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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.activity.Weather.WeatherActivity;
import com.example.project.activity.bio.BioActivity;
import com.example.project.activity.bio.BioEditActivity;
import com.example.project.database.UserProfile;
import com.google.android.material.navigation.NavigationView;

public class BmiActivity extends AppCompatActivity {

    private UserProfile user;
    private TextView bmiTextView;
    private float height;
    private float weight;
    private int range;
    private ActionBarDrawerToggle toggle;
    private boolean isDrawerFixed;

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

        user = new UserProfile(getApplicationContext());
        if (user.getHeight() != null && user.getWeight() > 0) {
            float feet = Float.parseFloat(user.getHeight().split("'")[0]);
            float inches = Float.parseFloat(user.getHeight().split("'")[1].split("\"")[0]);
            height = (feet * 12) + inches; // 1 foot / 12 inches
            weight = user.getWeight();
            float bmi = (weight / (height * height)) * 703;
            bmiTextView = findViewById(R.id.bmi_tv);
            bmiTextView.setText(String.format(java.util.Locale.US, "%.1f", bmi));
            if (bmi < 18.5 || bmi > 25.0) {
                bmiTextView.setTextColor(Color.RED);
            } else {
                bmiTextView.setTextColor(Color.parseColor("#29A100"));
            }
        }

        SeekBar seekBar = findViewById(R.id.seek_bar);
        range = 200;
        seekBar.setMax(range);
        seekBar.setProgress(range / 2);
        final TextView seekTextView = findViewById(R.id.seek_bar_value);

        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

            float adjustedWeight;

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
                float bmi = (adjustedWeight / (height * height)) * 703;
                if (bmi < 18.5 || bmi > 25.0) {
                    bmiTextView.setTextColor(Color.RED);
                } else {
                    bmiTextView.setTextColor(Color.parseColor("#29A100"));
                }
                bmiTextView.setText(String.format(java.util.Locale.US, "%.1f", bmi));
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


}
