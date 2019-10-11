package com.example.project.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.TileFragment;
import com.example.project.activity.bio.BioEditActivity;
import com.example.project.activity.login.SignInFragment;
import com.example.project.database.UserProfile;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements SignInFragment.inputPersist {
    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private ActionBarDrawerToggle toggle;
    private Context ctx;
    UserProfile userProfile;
    static String _name, _password = "";
    private boolean isDrawerFixed;
//    private Boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();

        // Handle navigation drawer
        isDrawerFixed = getResources().getBoolean(R.bool.isDrawerFixed);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
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

        //Find each frame layout, replace with corresponding fragment
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fl_test_frag, new TileFragment(), "Frag_1");
        fTrans.commit();

        // UserProfile will be our interface for interacting with the database
        userProfile = new UserProfile(ctx);
        if (!userProfile.isInDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        //TODO check for permissions before requesting
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

//         UNCOMMENT THIS TO ERASE CONTENTS OF DB
//        userProfile.upgrade();

        if (!userProfile.isLoggedIn()) {
            showLoginForm();
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
    public void onSaveSnapshot(String name, String password) {
        _name = name;
        _password = password;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AssetHandlers.loadProfileImage(this, menu, userProfile);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        userProfile = new UserProfile(ctx);
        if (!userProfile.isInDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if (!userProfile.isLoggedIn()) {
            showLoginForm();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.night_mode:
                if (!userProfile.isInDarkMode()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                userProfile.toggleDarkMode();
                userProfile.update();
                finish();
                startActivity(getIntent());
                return true;
            case R.id.edit_profile:
                Intent bioEdit = new Intent(this, BioEditActivity.class);
                startActivity(bioEdit);
                return true;
            case R.id.logout:
                userProfile.logout();
                _name = "";
                _password = "";
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showLoginForm() {
        SignInFragment dialog = new SignInFragment();
        Bundle args = new Bundle();
        args.putString("name", _name);
        args.putString("password", _password);
        dialog.setArguments(args);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "SignInFragment");
    }
}
