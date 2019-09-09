package com.example.project.activity.Weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Response;
import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.Requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    final String DARK_SKY_API_URL = "https://api.darksky.net/forecast/";
    final String DARK_SKY_API_KEY = "3ed44328d0b34c77cc6a6ee7ce334c3c";

    WeatherResponseListener weatherResponseListener;

    TextView cityText;
    TextView temperatureText;
    TextView summaryText;
    TextView humidityText;
    TextView windText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherResponseListener = new WeatherResponseListener(this);

        temperatureText = findViewById(R.id.temperatureText);
        summaryText = findViewById(R.id.summaryText);
        humidityText = findViewById(R.id.humidityText);
        windText = findViewById(R.id.windText);

        getPermissionsAndWeather();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                    @SuppressLint("MissingPermission")
//                    Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                    Location location = new Location("");
                    location.setLongitude(-111.89);
                    location.setLatitude(40.76);
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

        private void initializeWeatherFragment(String info, int containerID, int size) {
            WeatherFragment fragment = new WeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("json", info);
            bundle.putInt("size", size);
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(containerID, fragment);
            transaction.commit();
        }

//        custom conversion to interact with WeatherIconView library (100% is default)
        public int getIconSize(String stringSize) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            if (stringSize == "small") {
                return (width / 15);
            }
            if (stringSize == "large") {
                return (width / 6);
            }
            //shouldn't hit
            return -1;
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
                temperatureText.setText(roundStringWithMultiplier(current.getString("temperature"), 1));
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
                initializeWeatherFragment(weatherDays.getString(0), R.id.todayIcon, getIconSize("large"));
                initializeWeatherFragment(weatherDays.getString(1), R.id.day1Icon, getIconSize("small"));
                initializeWeatherFragment(weatherDays.getString(2), R.id.day2Icon, getIconSize("small"));
                initializeWeatherFragment(weatherDays.getString(3), R.id.day3Icon, getIconSize("small"));

            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}