package com.example.project.activity.bio;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.activity.BmiActivity;
import com.example.project.activity.CalorieActivity;
import com.example.project.activity.HikingActivity;
import com.example.project.activity.MainActivity;
import com.example.project.activity.Weather.WeatherActivity;
import com.example.project.database.UserProfile;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

public class BioActivity extends AppCompatActivity {

    UserProfile userProfile;
    TextView nameTV, ageTV, sexTV, heightTV, weightTV;
    ImageView imageView;
    private ActionBarDrawerToggle toggle;
    private boolean isDrawerFixed;

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

        imageView = findViewById(R.id.bio_img);

        populateInfo();

        Button editBioBtn = findViewById(R.id.edit_bio_btn);
        editBioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = view.getContext();
//                Toast toast = Toast.makeText(ctx, "Start Bio Activity", Toast.LENGTH_SHORT);
//                toast.show();
                Intent editBioPage = new Intent(ctx, BioEditActivity.class);
                startActivity(editBioPage);
            }
        });

        // Handle navigation drawer
        isDrawerFixed = getResources().getBoolean(R.bool.isDrawerFixed);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (!isDrawerFixed) {
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        }
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return handleNavigationEvent(item);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AssetHandlers.loadProfileImage(this, menu, userProfile);
        return true;
    }

    private void populateInfo() {
        nameTV.setText(userProfile.getName());
        heightTV.setText(userProfile.getHeight());
        weightTV.setText(userProfile.getWeight() + " lbs.");
        ageTV.setText(userProfile.getAge() + " yrs.");
        sexTV.setText(userProfile.getSex());

        String imgPath = userProfile.getImgPath();

        if (imgPath != null) {
            File imgFile = new File(userProfile.getImgPath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            } else {
                Toast.makeText(getApplicationContext(), "img path not found... " + userProfile.getImgPath(), Toast.LENGTH_LONG).show();
            }
        }
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
