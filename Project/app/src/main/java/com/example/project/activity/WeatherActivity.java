package com.example.project.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Response;
import com.example.project.R;
import com.example.project.Requests;
import com.github.pwittchen.weathericonview.WeatherIconView;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {
    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    final String OPEN_WEATHER_API_KEY = "8a85f0c871ca098af96f9408e91bb57d";

    WeatherResponseListener weatherResponseListener;
    WeatherIconListener weatherIconListener;

    WeatherIconView todayIcon;

    TextView cityText;
    TextView summaryText;
    TextView maxTempText;
    TextView minTempText;
    TextView humidityText;
    TextView windText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherResponseListener = new WeatherResponseListener();
        weatherIconListener = new WeatherIconListener();

        todayIcon = findViewById(R.id.todayIcon);

        cityText = findViewById(R.id.cityText);
        summaryText = findViewById(R.id.summaryText);
        maxTempText = findViewById(R.id.maxTempText);
        minTempText = findViewById(R.id.minTempText);
        humidityText = findViewById(R.id.humidityText);
        windText = findViewById(R.id.windText);

        cityText.setOnClickListener(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        todayIcon = findViewById(R.id.todayIcon);
        todayIcon.setIconResource(getString(R.string.wi_night_alt_lightning));
        //custom conversion to interact with WeatherIconView library (100% is default)
        todayIcon.setIconSize(width/5);
        todayIcon.setIconColor(Color.DKGRAY);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cityText:
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
//        api.openweathermap.org/data/2.5/forecast?q=London,us&mode=xml
        String url = "https://api.openweathermap.org/data/2.5/forecast?units=imperial&lat=" +
                latitude +
                "&lon=" +
                longitude +
                "&appid=" +
                OPEN_WEATHER_API_KEY;

        Requests.makeStringRequest(url, weatherResponseListener, this);
    }

    public void getWeatherIcon(String iconName) {
        String url = "https://openweathermap.org/img/wn/" +
                iconName +
                "@2x.png";
        Requests.makeImageRequest(url, weatherIconListener, this);
    }

    public class WeatherResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            Log.d("DATA: ", response);
//            String iconName;
//            try {
//                JSONObject json = new JSONObject(response);
//                iconName = json.getJSONArray("weather").getJSONObject(0).getString("icon");
//                cityText.setText(json.getString("name"));
//                summaryText.setText(json.getJSONArray("weather").getJSONObject (0).getString("description"));
////                maxTempText.setText(json.get("wind").toString());
////                minTempText.setText(json.get("wind").toString());
//                humidityText.setText("Humidity: " +
//                        json.getJSONObject("main").getString("humidity") +
//                        "%");
//                windText.setText("Wind Speed: " +
//                        json.getJSONObject("wind").getString("speed") +
//                        " mph");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return;
//            }
//
//            getWeatherIcon(iconName);
        }
    }

    public class WeatherIconListener implements  Response.Listener<Bitmap> {

        @Override
        public void onResponse(Bitmap response) {
            Log.d("hit", "hit");
//            todayIcon.setImageBitmap(response);

        }
    }

}