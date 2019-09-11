package com.example.project.activity.bio;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.database.UserProfile;

public class BioActivity extends AppCompatActivity {

    UserProfile userProfile;
    TextView nameTV, ageTV, sexTV, heightTV, weightTV, cityTV, countryTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        userProfile = new UserProfile(getApplicationContext());
        nameTV = findViewById(R.id.bio_name);
        ageTV = findViewById(R.id.bio_age);
        sexTV = findViewById(R.id.bio_sex);
        heightTV = findViewById(R.id.bio_height);
        weightTV = findViewById(R.id.bio_weight);
        cityTV = findViewById(R.id.bio_city);
        countryTV = findViewById(R.id.bio_country);

        populateInfo();

        Button editBioBtn = findViewById(R.id.edit_bio_btn);
        editBioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = view.getContext();
                Toast toast = Toast.makeText(ctx, "Start Bio Activity", Toast.LENGTH_SHORT);
                toast.show();
//                Intent editBioPage = new Intent(ctx, BioEditActivity.class);
//                startActivity(editBioPage);
            }
        });
    }

    private void populateInfo() {
        nameTV.setText(userProfile.getName());
        cityTV.setText(userProfile.getCity());
        countryTV.setText(userProfile.getCountry());
        heightTV.setText(userProfile.getHeight());
        weightTV.setText(Integer.toString(userProfile.getWeight()));
        ageTV.setText(userProfile.getAge());
        sexTV.setText(userProfile.getSex());
    }
}
