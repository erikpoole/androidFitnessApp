package com.example.project.activity.Weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
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
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Response;
import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.Requests;
import com.github.pwittchen.weathericonview.WeatherIconView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {
    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    //TODO: Delete OPEN_WEATHER_API_KEY
//    final String OPEN_WEATHER_API_KEY = "8a85f0c871ca098af96f9408e91bb57d";
    final String DARK_SKY_API_URL = "https://api.darksky.net/forecast/";
    final String DARK_SKY_API_KEY = "3ed44328d0b34c77cc6a6ee7ce334c3c";

    WeatherResponseListener weatherResponseListener;

    WeatherIconView todayIcon;
    TextView cityText;
    TextView temperatureText;
    TextView summaryText;
    TextView maxTempText;
    TextView minTempText;
    TextView humidityText;
    TextView windText;

    WeatherFragment day1Fragment;
    WeatherFragment day2Fragment;
    WeatherFragment day3Fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherResponseListener = new WeatherResponseListener(this);

        todayIcon = findViewById(R.id.todayIcon);
        cityText = findViewById(R.id.cityText);
        temperatureText = findViewById(R.id.temperatureText);
        summaryText = findViewById(R.id.summaryText);
        maxTempText = findViewById(R.id.maxTempText);
        minTempText = findViewById(R.id.minTempText);
        humidityText = findViewById(R.id.humidityText);
        windText = findViewById(R.id.windText);

        cityText.setOnClickListener(this);

        todayIcon.setIconSize(getIconSize());

        day1Fragment = new WeatherFragment();
        day2Fragment = new WeatherFragment();
        day3Fragment = new WeatherFragment();

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
        String url = DARK_SKY_API_URL +
                DARK_SKY_API_KEY +
                "/" +
                latitude +
                "," +
                longitude +
                "?exclude=minutely,hourly,alerts,flags";

        Requests.makeStringRequest(url, weatherResponseListener, this);
    }

    //custom conversion to interact with WeatherIconView library (100% is default)
    public int getIconSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return (width / 5);
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

        private int getStringIdentifier(String name) {
            return context.getResources().getIdentifier(name, "string", context.getPackageName());
        }

        private String getIconCode(JSONObject day) {
            try {
                String rawIcon = day.getString("icon");
                String convertedIcon = weatherIcons.getString(rawIcon);
                return getString(getStringIdentifier(convertedIcon));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        private String roundStringWithMultiplier(String input, int multiplier) {
            return String.valueOf(Math.round(Double.parseDouble(input) * multiplier));
        }

        @Override
        public void onResponse(String response) {
            Log.d("DATA: ", response);
            try {
                JSONObject json = new JSONObject(response);
                JSONObject current = json.getJSONObject("currently");
                JSONArray weatherDays = json.getJSONObject("daily").getJSONArray("data");

                //from current forecast
                todayIcon.setIconResource(getIconCode(current));

                temperatureText.setText(
                        "Temperature: " +
                        roundStringWithMultiplier(current.getString("temperature"), 1) +
                        "\u00B0F");
                humidityText.setText(
                        "Humidity: " +
                        roundStringWithMultiplier(current.getString("humidity"), 100) +
                        "%");
                windText.setText(
                        "Wind Speed: " +
                                roundStringWithMultiplier(current.getString("windSpeed"), 1) +
                        " mph");

                //from weekly forcast
                summaryText.setText(weatherDays.getJSONObject(0).getString("summary"));
                maxTempText.setText(
                        "High: " +
                        roundStringWithMultiplier(weatherDays.getJSONObject(0).getString("temperatureHigh"), 1) +
                        "\u00B0F");
                minTempText.setText(
                        "Low: " +
                        roundStringWithMultiplier(weatherDays.getJSONObject(0).getString("temperatureLow"), 1) +
                        "\u00B0F");


//                cityText.setText(json.getString("name"));

                Bundle day1Data = new Bundle();
                day1Data.putString("json", weatherDays.getString(1));
                day1Fragment.setArguments(day1Data);

                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.day1Icon, day1Fragment);
                transaction1.commit();


                Bundle day2Data = new Bundle();
                day2Data.putString("json", weatherDays.getString(1));
                day2Fragment.setArguments(day2Data);

                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.day2Icon, day2Fragment);
                transaction2.commit();


                Bundle day3Data = new Bundle();
                day3Data.putString("json", weatherDays.getString(1));
                day3Fragment.setArguments(day3Data);

                FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                transaction3.replace(R.id.day3Icon, day3Fragment);
                transaction3.commit();

            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
    }


}