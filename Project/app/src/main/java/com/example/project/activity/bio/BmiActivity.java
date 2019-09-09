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

import com.example.project.R;

public class BmiActivity extends AppCompatActivity {

    private UserProfile user;
    private TextView bmiTextView;
    private SeekBar simpleSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("BMI");

        TextView bmiTextView = findViewById(R.id.bmi_tv);

        SeekBar simpleSeekBar = findViewById(R.id.seek_bar);
        simpleSeekBar.setMax(100);
        simpleSeekBar.setProgress(33);
    }

}
