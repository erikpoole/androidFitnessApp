package com.example.project.activity.bio;

import android.os.Bundle;

import com.example.project.database.UserProfile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;

public class BmiActivity extends AppCompatActivity {

    private UserProfile user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("BMI");

        //The formula for BMI is weight in kilograms divided by height in meters squared
        user = new UserProfile(getApplicationContext());
        if (user != null) {
            float kilogramsToPounds = 0.453592F; // 0.453592F kg / 1 lb
            float weight = (float) user.getWeight() * kilogramsToPounds;
            float feetToMeters = 0.3048F; // 1 ft / 0.3048 meters
            float height = Float.parseFloat(user.getHeight()) * feetToMeters;
            float bmi = weight / height;
            TextView bmiTextView = findViewById(R.id.bmi_tv);
            bmiTextView.setText(String.format(java.util.Locale.US, "%.1f", bmi));
        }

        SeekBar simpleSeekBar = findViewById(R.id.seek_bar);
        simpleSeekBar.setMax(100);
        simpleSeekBar.setProgress(33);
    }

}
