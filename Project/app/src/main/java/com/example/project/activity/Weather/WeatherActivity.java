package com.example.project.activity.Weather;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.UserViewModel;
import com.example.project.WeatherViewModel;
import com.example.project.activity.MainActivity;
import com.example.project.activity.bio.BioEditActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;


public class WeatherActivity extends AppCompatActivity {
    private WeatherViewModel weatherViewModel;
    private UserViewModel userViewModel;

    private TextView temperatureText;
    private TextView summaryText;
    private TextView humidityText;
    private TextView windText;

    private JSONObject weatherIcons;

    private ActionBarDrawerToggle toggle;
    private boolean isDrawerFixed;
    private Boolean darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        userViewModel = new UserViewModel(this.getApplication());
        weatherViewModel = new WeatherViewModel(this.getApplication());

        weatherViewModel.getTemperature().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String temperature) {
                temperatureText.setText(temperature + "\u00B0F");
            }
        });

        weatherViewModel.getHumidity().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String humidity) {
                humidityText.setText("Humidity:\n" + humidity + "%");
            }
        });

        weatherViewModel.getWindSpeed().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String windSpeed) {
                windText.setText("Wind Speed:\n" + windSpeed + " mph");
            }
        });

        weatherViewModel.getSummary().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String summary) {
                summaryText.setText(summary);
            }
        });

        userViewModel.isInDarkMode().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean mDarkMode) {
                if (mDarkMode != null) {
                    darkMode = mDarkMode;
                }
            }
        });

        try {
            weatherIcons = new JSONObject(AssetHandlers.readAsset("weatherIcons.json", this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        temperatureText = findViewById(R.id.temperatureText);
        summaryText = findViewById(R.id.summaryText);
        humidityText = findViewById(R.id.humidityText);
        windText = findViewById(R.id.windText);

        setupFragments();

        // Handle navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Weather");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (!isDrawerFixed) {
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        }
        NavigationView nav = findViewById(R.id.nav_view);
        final Activity activity = this;
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return AssetHandlers.handleNavigationEvent(activity, item);
            }
        });
        isDrawerFixed = getResources().getBoolean(R.bool.isDrawerFixed);
    }

    private void initializeWeatherFragment(int dayNumber, int containerID, String size) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("dayNumber", dayNumber);
        bundle.putString("size", size);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, fragment);
        transaction.commit();
    }

    private void setupFragments() {
        initializeWeatherFragment(0, R.id.todayIcon, "large");
        initializeWeatherFragment(1, R.id.day1Icon, "small");
        initializeWeatherFragment(2, R.id.day2Icon, "small");
        initializeWeatherFragment(3, R.id.day3Icon, "small");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AssetHandlers.loadProfileImage(this, menu, userViewModel);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.night_mode:
                if (!darkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    userViewModel.updateDarkMode(true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    userViewModel.updateDarkMode(false);
                }
                finish();
                startActivity(getIntent());
                return true;
            case R.id.edit_profile:
                Intent bioEdit = new Intent(this, BioEditActivity.class);
                startActivity(bioEdit);
                return true;
            case R.id.logout:
                userViewModel.logout();
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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