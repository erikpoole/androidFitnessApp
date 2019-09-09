package com.example.project.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.R;
import com.example.project.TileFragment;
import com.example.project.activity.Weather.WeatherActivity;
import com.example.project.activity.bio.BioActivity;
import com.example.project.activity.bio.BioEditActivity;
import com.example.project.activity.bio.BmiActivity;
import com.example.project.activity.bio.CalorieActivity;
import com.example.project.activity.login.SignInFragment;
import com.example.project.database.UserProfile;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle toggle;
    private Context ctx;
    UserProfile userProfile;
//    private Boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();

        // Handle navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return handleNavigationEvent(item);
            }
        });

        //Find each frame layout, replace with corresponding fragment
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fl_test_frag, new TileFragment(), "Frag_1");
        fTrans.commit();

        // UserProfile will be our interface for interacting with the database
        userProfile = new UserProfile(ctx);
//        userProfile.update();
        if (!userProfile.isLoggedIn()) {
            showLoginForm();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings: // TODO need to create a settings page
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                return true;
            case R.id.edit_profile: // TODO this doesn't quite work yet
                Intent bioEdit = new Intent(this, BioEditActivity.class);
                startActivity(bioEdit);
                Toast.makeText(getApplicationContext(), "This page is still under construction.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.logout:
                userProfile.logout();
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private void showLoginForm() {
        SignInFragment dialog = new SignInFragment();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "SignInFragment");
    }
}
