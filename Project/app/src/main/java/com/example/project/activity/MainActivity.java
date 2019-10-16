package com.example.project.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.example.project.AssetHandlers;
import com.example.project.R;
import com.example.project.TileFragment;
import com.example.project.UserViewModel;
import com.example.project.activity.Weather.WeatherActivity;
import com.example.project.activity.bio.BioEditActivity;
import com.example.project.activity.login.SignInFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements SignInFragment.inputPersist {
    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private UserViewModel userViewModel;

    private ActionBarDrawerToggle toggle;
    private Context ctx;
    static String _name, _password = "";
    private boolean isDrawerFixed;
    public Boolean loggedIn;
    private Boolean darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();
        userViewModel = new UserViewModel(this.getApplication());

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

        if (getIntent().getExtras() != null) {
            darkMode = getIntent().getExtras().getBoolean("darkMode");
        }

        //Dark mode!
        userViewModel.isInDarkMode().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean mDarkMode) {
                if (mDarkMode != null) {
                    if (darkMode != mDarkMode) {
                        darkMode = mDarkMode;
                        if (darkMode) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        }
                        Intent intent = getIntent();
                        intent.putExtra("darkMode", darkMode);

                        finish();
                        startActivity(intent);
                    }


                }
            }
        });

//        SignInFragment dialog = new SignInFragment();
//        Bundle args = new Bundle();
//        args.putString("name", _name);
//        args.putString("password", _password);
//        dialog.setArguments(args);
//        dialog.setCancelable(false);
//        dialog.show(getSupportFragmentManager(), "SignInFragment");

//        Intent weatherPage = new Intent(activity.getApplicationContext(), WeatherActivity.class);
//        activity.startActivity(weatherPage);
//        return true;

            //TODO check for permissions before requesting
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String name) {
                // Update the UI, in this case, a TextView.
                if (name != null) {
                    Toast.makeText(ctx, "Hello " + name + "!", Toast.LENGTH_LONG).show();
                } else {
                    showLoginForm();
                }
            }
        };

        userViewModel.hasUserLoggedIn().observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean mLoggedIn) {
                if (mLoggedIn != null) {
                    loggedIn = mLoggedIn;
                }
            }
        });

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        userViewModel.getName().observe(this, nameObserver);

        if (loggedIn != null && !loggedIn) {
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
        AssetHandlers.loadProfileImage(this, menu, userViewModel);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.night_mode:
                if (darkMode) {
                    userViewModel.updateDarkMode(false);
                } else {
                    userViewModel.updateDarkMode(true);
                }
                return true;
            case R.id.edit_profile:
                Intent bioEdit = new Intent(this, BioEditActivity.class);
                startActivity(bioEdit);
                return true;
            case R.id.logout:
                userViewModel.logout();
                _name = "";
                _password = "";
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
