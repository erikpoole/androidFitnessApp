package com.example.project.activity.bio;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.project.R;
import com.example.project.activity.BmiActivity;
import com.example.project.activity.CalorieActivity;
import com.example.project.activity.HikingActivity;
import com.example.project.activity.MainActivity;
import com.example.project.activity.Weather.WeatherActivity;
import com.example.project.database.UserProfile;
import com.google.android.material.navigation.NavigationView;

public class BioEditActivity extends AppCompatActivity implements BioFormFragment.onSubmitFormListener {

    UserProfile userProfile;
    TextView nameTV;
    ImageView imageView;
    Context ctx;
    private ActionBarDrawerToggle toggle;
    private boolean isDrawerFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_edit);
        ctx = getApplicationContext();
        userProfile = new UserProfile(ctx);

        nameTV = findViewById(R.id.bio_edit_name);
        nameTV.setText(userProfile.getName());

        // Handle navigation drawer
        isDrawerFixed = getResources().getBoolean(R.bool.isDrawerFixed);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Edit Profile");
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

    public void onSubmitForm(String sex, String height, int weight, String imgPath) {
        userProfile.setWeight(weight);
        userProfile.setHeight(height);
        userProfile.setSex(sex);
        userProfile.setImgPath(imgPath);
        userProfile.update();
        Intent bio = new Intent(this, BioActivity.class);
        startActivity(bio);
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
