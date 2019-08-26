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
import android.widget.Toast;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {
    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    final String OPEN_WEATHER_API_KEY = "8a85f0c871ca098af96f9408e91bb57d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Button weatherButton = findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(this);
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
        Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_LONG).show();
        Log.d("DATA:", location.toString());
        makeRequest(location);
    }


    //todo put in thread: http://mobiledevtuts.com/android/android-http-with-asynctask-example/
    public void makeRequest(Location location) {
        URL url = null;
        try {
            url = new URL(("https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid="+OPEN_WEATHER_API_KEY));
        } catch (MalformedURLException e) {
            Log.d("DATA:", "bad url");
        }
        HttpsURLConnection urlConnection = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
//            InputStream in = new BufferedInputStream((urlConnection.getInputStream()));
//            Log.d("DATA:", in.toString());
        } catch (IOException e) {
            Log.d("DATA:", "bad connection");
        } finally {
            urlConnection.disconnect();
        }
    }

    //TODO test API Key
//        https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22



//                Uri webpage = Uri.parse("http://weather.com/weather/tenday/l/Salt+Lake+City+UT+USUT0225:1:US");
//                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
//                startActivity(webIntent);

//    URL url = new URL("http://www.android.com/");
//    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//   try {
//        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//        readStream(in);
//    } finally {
//        urlConnection.disconnect();
//    }

}