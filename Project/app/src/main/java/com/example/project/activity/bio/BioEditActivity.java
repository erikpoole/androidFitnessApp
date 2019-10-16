package com.example.project.activity.bio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;

import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.UserViewModel;
import com.example.project.activity.MainActivity;
import com.google.android.material.navigation.NavigationView;

public class BioEditActivity extends AppCompatActivity implements BioFormFragment.onSubmitFormListener {
    private UserViewModel userViewModel;
    TextView nameTV;
    ImageView imageView;
    Context ctx;
    private ActionBarDrawerToggle toggle;
    private boolean isDrawerFixed;
    private String name;
    private Boolean darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_edit);
        ctx = getApplicationContext();

        userViewModel.getName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String mName) {
                if (mName != null) {
                    name = mName;
                }
            }
        });

        nameTV = findViewById(R.id.bio_edit_name);
        nameTV.setText(name);

        userViewModel = new UserViewModel(this.getApplication());

        // Handle navigation drawer
        isDrawerFixed = getResources().getBoolean(R.bool.isDrawerFixed);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Edit Profile");
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

        userViewModel.isInDarkMode().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean mDarkMode) {
                if (mDarkMode != null) {
                    darkMode = mDarkMode;
                }
            }
        });
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
                if (!darkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    userViewModel.updateDarkMode(true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    userViewModel.updateDarkMode(false);
                }
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

    public void onSubmitForm(String sex, String height, int weight, String imgPath) {
        userViewModel.updateSex(sex);
        userViewModel.updateHeight(height);
        userViewModel.updateWeight(weight);
//        Toast.makeText(getApplicationContext(), imgPath, Toast.LENGTH_LONG).show();
        userViewModel.updateImgPath(imgPath);
        Intent bio = new Intent(this, BioActivity.class);
        startActivity(bio);
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
