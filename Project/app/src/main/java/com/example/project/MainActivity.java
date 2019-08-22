package com.example.project;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

//        Intent weatherPage = new Intent(this, WeatherActivity.class);
//        startActivity(weatherPage);
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Toast.makeText(getApplicationContext(), "Item selected", Toast.LENGTH_LONG).show();
//
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.nav_home:
//                Toast.makeText(getApplicationContext(), "Go Home Not Implemented!", Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.nav_weather:
//                Toast.makeText(getApplicationContext(), "Go Weather", Toast.LENGTH_LONG).show();
//                Intent weatherPage = new Intent(this, WeatherActivity.class);
//                startActivity(weatherPage);
//                return true;
//            case R.id.nav_hiking:
//                Toast.makeText(getApplicationContext(), "Go Hiking", Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
