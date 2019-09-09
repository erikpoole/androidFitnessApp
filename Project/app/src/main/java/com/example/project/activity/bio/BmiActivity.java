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

        user = new UserProfile(getApplicationContext());
        if (user != null) {
            float feet = Float.parseFloat(user.getHeight().split("'")[0]);
            float inches = Float.parseFloat(user.getHeight().split("'")[1].split("\"")[0]);
            float height = (feet * 12) + inches; // 1 foot / 12 inches
            float weight = user.getWeight();
            float bmi = (weight / (height * height)) * 703;
            TextView bmiTextView = findViewById(R.id.bmi_tv);
            bmiTextView.setText(String.format(java.util.Locale.US, "%.1f", bmi));
            Toast.makeText(getApplicationContext(), weight + " " + height, Toast.LENGTH_LONG).show();
        }

        SeekBar simpleSeekBar = findViewById(R.id.seek_bar);
        simpleSeekBar.setMax(100);
        simpleSeekBar.setProgress(33);
    }

}
