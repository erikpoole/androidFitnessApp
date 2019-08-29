package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HikingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiking);

        Button hikingButton = findViewById(R.id.hikingButton);
        hikingButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        String currentLocation = getLocation();
//        if (currentLocation.isEmpty()) {
//            Toast.makeText(getApplicationContext(), "Can't obtain location.", Toast.LENGTH_LONG).show();
//            return;
//        }
//        Toast.makeText(getApplicationContext(), currentLocation, Toast.LENGTH_LONG).show();

        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4192?q=" + Uri.encode("1st & Pike, Seattle"));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    // TODO this method needs lots of work
    public String getLocation() {
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
//            return "";
//        }
//        return locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).toString();
        return "";
    }
}
