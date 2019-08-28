package com.example.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {
    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    final String OPEN_WEATHER_API_KEY = "8a85f0c871ca098af96f9408e91bb57d";

    TextView weatherText;
    TextView temperateText;
    TextView windText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Button weatherButton = findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(this);

        weatherText = findViewById(R.id.weatherText);
        temperateText = findViewById(R.id.temperatureText);
        windText = findViewById(R.id.windText);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weatherButton:
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

    public void getWeather(Location location) {
        if (location == null) {
            Toast.makeText(
                    getApplicationContext(),
                    "Can't get location, try again later",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        String latitude = Double.toString(location.getLatitude());
        String longitude = Double.toString(location.getLongitude());
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" +
                        latitude +
                        "&lon=" +
                        longitude +
                        "&appid=" +
                        OPEN_WEATHER_API_KEY;

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DATA: ", response);
                handleWeatherResponse(response);
            }
        };
        Requests.makeRequest(url, listener, this);
    }

    public void handleWeatherResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            weatherText.setText(json.get("weather").toString());
            temperateText.setText(json.get("main").toString());
            windText.setText(json.get("wind").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}