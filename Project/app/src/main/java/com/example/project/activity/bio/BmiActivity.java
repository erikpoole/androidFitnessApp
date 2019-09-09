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
    private TextView bmiTextView;
    private float height;
    private float weight;

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
            height = (feet * 12) + inches; // 1 foot / 12 inches
            weight = user.getWeight();
            float bmi = (weight / (height * height)) * 703;
            bmiTextView = findViewById(R.id.bmi_tv);
            bmiTextView.setText(String.format(java.util.Locale.US, "%.1f", bmi));
        }

        SeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setMax(100);
        seekBar.setProgress(33);
        final TextView seekTextView = findViewById(R.id.seek_bar_value);

        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

            float adjustedWeight;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustedWeight = weight;
                int change = progress - 50;
                seekTextView.setText(change + " lbs");
                adjustedWeight += change;
                float bmi = (adjustedWeight / (height * height)) * 703;
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

}
