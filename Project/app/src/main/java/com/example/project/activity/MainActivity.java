package com.example.project.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.R;
import com.example.project.SignInFragment;
import com.example.project.TileFragment;
import com.example.project.activity.bio.BioActivity;
import com.example.project.activity.bio.BioHelperDB;
import com.example.project.activity.bio.BioInfoContract;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle toggle;
    private BioHelperDB dbHelper;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        NavigationView nav  = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return handleNavigationEvent(item);
            }
        });

        //Find each frame layout, replace with corresponding fragment
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fl_test_frag, new TileFragment(),"Frag_1");
        fTrans.commit();

        //Init SQLite
        ctx = getApplicationContext();
        dbHelper = new BioHelperDB(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        dbHelper.onUpgrade(db, 1, 1);

        // Has to happen after Init SQLite
        greetUser();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                Toast.makeText(getApplicationContext(), "Settings page not implemented!", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean handleNavigationEvent(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_home:
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                return true;
            case R.id.nav_weather:
                Intent weatherPage = new Intent(this, WeatherActivity.class);
                startActivity(weatherPage);
                return true;
            case R.id.nav_hiking:
                Intent hikingPage = new Intent(this, HikingActivity.class);
                startActivity(hikingPage);
                return true;
            case R.id.nav_bio:
                Intent bioPage = new Intent(this, BioActivity.class);
                startActivity(bioPage);
                return true;
            default:
                return false;
        }
    }

    private void greetUser() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // COMMENTS FROM SQLite ANDROID DOCS
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                BioInfoContract.BioEntry.USER_NAME,
                BioInfoContract.BioEntry.IMG_PATH,
                BioInfoContract.BioEntry.ACTIVE_STATE,
                BioInfoContract.BioEntry.GOAL,
                BioInfoContract.BioEntry.IS_LOGGED_IN
        };

        String selection = BioInfoContract.BioEntry.IS_LOGGED_IN + " = ?";
        String[] selectionArgs = { "1" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                BioInfoContract.BioEntry.USER_NAME + " ASC";

        Cursor cursor = db.query(
                BioInfoContract.BioEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        int rowID, isLoggedIn;
        String name = "";
        String imgPath, activeState, goal;
        if (cursor.moveToFirst()) {
            rowID = cursor.getInt(0);
            name = cursor.getString(1);
            imgPath = cursor.getString(2);
            activeState = cursor.getString(3);
            goal = cursor.getString(4);
            isLoggedIn = cursor.getInt(5);
        } else {
            cursor.close();
            db.close();
//                Toast toast = Toast.makeText(ctx, "Nobody Logged In!" , Toast.LENGTH_SHORT);
//                toast.show();
            SignInFragment dialog = new SignInFragment();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "SignInFragment");
            return;
        }
        Toast toast = Toast.makeText(ctx, "Welcome Back " + name , Toast.LENGTH_SHORT);
        toast.show();
        cursor.close();
        db.close();
    }
}
