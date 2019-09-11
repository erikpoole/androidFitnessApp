package com.example.project.activity.bio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.database.UserProfile;

public class BioEditActivity extends AppCompatActivity implements BioFormFragment.onSubmitFormListener {

    UserProfile userProfile;
    TextView nameTV, ageTV;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_edit);
        ctx = getApplicationContext();
        userProfile = new UserProfile(ctx);

        nameTV = findViewById(R.id.bio_edit_name);
        ageTV = findViewById(R.id.bio_edit_age);

        nameTV.setText(userProfile.getName());
        ageTV.setText(userProfile.getAge());
    }

    public void onSubmitForm(String sex, String height, int weight, String imgPath) {
        userProfile.setWeight(weight);
        userProfile.setHeight(height);
        userProfile.setSex(sex);
        userProfile.setImgPath(imgPath);
        userProfile.update();
        Intent bio = new Intent(this, BioActivity.class);
        startActivity(bio);
    }
}
