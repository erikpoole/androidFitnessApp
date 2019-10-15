package com.example.project.activity.bio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.example.project.database.UserProfile;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

public class BioActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    TextView nameTV, ageTV, sexTV, heightTV, weightTV;
    String nameVal, ageVal, sexVal, imgPathVal = "";
    ImageView imageView;
    private ActionBarDrawerToggle toggle;
    private boolean isDrawerFixed;
    private Boolean darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        nameTV = findViewById(R.id.bio_name);
        ageTV = findViewById(R.id.bio_age);
        sexTV = findViewById(R.id.bio_sex);
        heightTV = findViewById(R.id.bio_height);
        weightTV = findViewById(R.id.bio_weight);

        imageView = findViewById(R.id.bio_img);

        userViewModel = new UserViewModel(this.getApplication());

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        userViewModel.getName().observe(this, nameObserver);
        userViewModel.getAge().observe(this, ageObserver);
        userViewModel.getSex().observe(this, sexObserver);
        userViewModel.getHeight().observe(this, heightObserver);
        userViewModel.getWeight().observe(this, weightObserver);
        userViewModel.getImgPath().observe(this, imgPathObserver);

        Button editBioBtn = findViewById(R.id.edit_bio_btn);
        editBioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = view.getContext();
                Intent editBioPage = new Intent(ctx, BioEditActivity.class);
                Bundle editBundle = new Bundle();
                editBundle.putString("name", nameVal);
                editBioPage.putExtras(editBundle);
                startActivity(editBioPage);
            }
        });

        // Handle navigation drawer
        isDrawerFixed = getResources().getBoolean(R.bool.isDrawerFixed);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Profile");
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

    final Observer<String> nameObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable final String name) {
            // Update the UI, in this case, a TextView.
            if (name != null) {
                nameVal = name;
                nameTV.setText(name);
            }
        }
    };

    final Observer<String> heightObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable final String height) {
            // Update the UI, in this case, a TextView.
            if (height != null) {
                heightTV.setText(height);
            }
        }
    };

    final Observer<Integer> weightObserver = new Observer<Integer>() {
        @Override
        public void onChanged(@Nullable final Integer weight) {
            // Update the UI, in this case, a TextView.
            if (weight != null) {
                weightTV.setText(weight + " lbs.");
            }
        }
    };

    final Observer<String> ageObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable final String age) {
            // Update the UI, in this case, a TextView.
            if (age != null) {
                ageVal = age;
                ageTV.setText(age);
            }
        }
    };

    final Observer<String> sexObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable final String sex) {
            // Update the UI, in this case, a TextView.
            if (sex != null) {
                sexVal = sex;
                sexTV.setText(sex);
            }
        }
    };

    final Observer<String> imgPathObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable final String imgPath) {
            // Update the UI, in this case, a TextView.
            if (imgPath != null) {
                imgPathVal = imgPath;
                File imgFile = new File(imgPath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                } else {
                    Toast.makeText(getApplicationContext(), "img path not found... " + imgPath, Toast.LENGTH_LONG).show();
                }
            }
        }
    };

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
