package com.example.project.activity.Weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Response;
import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.Requests;
import com.example.project.activity.HikingActivity;
import com.example.project.activity.MainActivity;
import com.example.project.activity.bio.BioActivity;
import com.example.project.activity.BmiActivity;
import com.example.project.activity.CalorieActivity;
import com.example.project.activity.bio.BioEditActivity;
import com.example.project.database.UserProfile;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    final String DARK_SKY_API_URL = "https://api.darksky.net/forecast/";
    final String DARK_SKY_API_KEY = "3ed44328d0b34c77cc6a6ee7ce334c3c";

    WeatherResponseListener weatherResponseListener;

    TextView temperatureText;
    TextView summaryText;
    TextView humidityText;
    TextView windText;

    UserProfile user;

    private ActionBarDrawerToggle toggle;
    private boolean isDrawerFixed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherResponseListener = new WeatherResponseListener(this);

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
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return AssetHandlers.handleNavigationEvent(this, item);
            }
        });
        isDrawerFixed = getResources().getBoolean(R.bool.isDrawerFixed);

        getPermissionsAndWeather();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AssetHandlers.loadProfileImage(this, menu, user);
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
                UserProfile userProfile = new UserProfile(getApplicationContext());
                userProfile.logout();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    @SuppressLint("MissingPermission")
                    Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                    getWeather(location);
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Can't access location!  Please enable location permissions to continue!",
                            Toast.LENGTH_LONG)
                            .show();
                }
                return;
            }
        }
    }

    public void getPermissionsAndWeather() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            Toast.makeText(getApplicationContext(), "Requesting Location Access...", Toast.LENGTH_LONG).show();
            return;
        } else {
            Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            getWeather(location);
            return;
        }
    }

    public void getWeather(Location location) {
        //handles first time access if getLastKnownLocation doesn't exist
        if (location == null) {
            Log.d("DATA:", "getWeather: default value used...");
            location = new Location("");
            location.setLongitude(-111.89);
            location.setLatitude(40.76);
        }

        String latitude = Double.toString(location.getLatitude());
        String longitude = Double.toString(location.getLongitude());
        String url = DARK_SKY_API_URL +
                DARK_SKY_API_KEY +
                "/" +
                latitude +
                "," +
                longitude +
                "?exclude=minutely,hourly,alerts,flags";

        Requests.makeStringRequest(url, weatherResponseListener, this);
    }

    public class WeatherResponseListener implements Response.Listener<String> {
        Context context;
        JSONObject weatherIcons;

        public WeatherResponseListener(Context inputContext) {
            context = inputContext;
            try {
                weatherIcons = new JSONObject(AssetHandlers.readAsset("weatherIcons.json", context));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

        @Override
        public void onResponse(String response) {
            Log.d("DATA: ", response);
            try {
                JSONObject json = new JSONObject(response);
                JSONObject current = json.getJSONObject("currently");
                JSONArray weatherDays = json.getJSONObject("daily").getJSONArray("data");

//                cityText.setText(json.getString("name"));

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
    }
}