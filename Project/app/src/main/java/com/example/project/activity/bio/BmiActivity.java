package com.example.project.activity.bio;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import com.example.project.activity.HikingActivity;
import com.example.project.activity.MainActivity;
import com.example.project.activity.Weather.WeatherActivity;
import com.example.project.database.UserProfile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;

public class BmiActivity extends AppCompatActivity {

    private UserProfile user;
    private TextView bmiTextView;
    private float height;
    private float weight;
    private int range;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);

        // Handle navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("BMI");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return handleNavigationEvent(item);
            }
        });

        user = new UserProfile(getApplicationContext());
        if (user != null) {
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
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }


}
