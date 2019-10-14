package com.example.project.activity.Weather;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProviders;

import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.Repository;
import com.example.project.UserViewModel;
import com.example.project.WeatherDBEntity;
import com.example.project.WeatherViewModel;
import com.example.project.activity.MainActivity;
import com.example.project.activity.bio.BioEditActivity;
import com.example.project.database.UserProfile;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
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

    private UserProfile user;

    private ActionBarDrawerToggle toggle;
    private boolean isDrawerFixed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        userViewModel = new UserViewModel(this.getApplication());
        weatherViewModel = new WeatherViewModel(this.getApplication());
        weatherViewModel.getWeather().observe(this, new Observer<WeatherDBEntity>() {
            @Override
            public void onChanged(WeatherDBEntity weatherDBEntity) {
                if (weatherDBEntity != null) {
                    handleWeatherJSON(weatherDBEntity.getWeatherJson());
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

        user = new UserProfile(getApplicationContext());

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

    private String roundStringWithMultiplier(String input, int multiplier) {
        return String.valueOf(Math.round(Double.parseDouble(input) * multiplier));
    }

    private void initializeWeatherFragment(String info, int containerID, String size) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("json", info);
        bundle.putString("size", size);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, fragment);
        transaction.commit();
    }

    private void handleWeatherJSON(String inputString) {
        Log.d("DATA: ", inputString);
        try {
            JSONObject json = new JSONObject(inputString);
            JSONObject current = json.getJSONObject("currently");
            JSONArray weatherDays = json.getJSONObject("daily").getJSONArray("data");

            //from current forecast
            temperatureText.setText(
                    roundStringWithMultiplier(current.getString("temperature"), 1) +
                            "\u00B0F"
            );
            humidityText.setText(
                    "Humidity:\n" +
                            roundStringWithMultiplier(current.getString("humidity"), 100) +
                            "%");
            windText.setText(
                    "Wind Speed:\n" +
                            roundStringWithMultiplier(current.getString("windSpeed"), 1) +
                            " mph");

            //from weekly forcast
            summaryText.setText(weatherDays.getJSONObject(0).getString("summary"));

            //index 0 is current date
            initializeWeatherFragment(weatherDays.getString(0), R.id.todayIcon, "large");
            initializeWeatherFragment(weatherDays.getString(1), R.id.day1Icon, "small");
            initializeWeatherFragment(weatherDays.getString(2), R.id.day2Icon, "small");
            initializeWeatherFragment(weatherDays.getString(3), R.id.day3Icon, "small");

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

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
                if (!user.isInDarkMode()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                user.toggleDarkMode();
                user.update();
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