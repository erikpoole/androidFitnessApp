package com.example.project.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.project.TileFragment;
import com.example.project.activity.bio.BioHelperDB;
import com.example.project.R;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle toggle;
    private BioHelperDB dbHelper;

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
        dbHelper = new BioHelperDB(getApplicationContext());

        //Redirect to right page
//        startBioActivity();
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
//                startBioActivity();
                return true;
            default:
                return false;
        }
    }

//    private void startBioActivity() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        // COMMENTS FROM SQLite ANDROID DOCS
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {
//                BaseColumns._ID,
//                BioInfoContract.BioEntry.USER_NAME,
//                BioInfoContract.BioEntry.CITY,
//                BioInfoContract.BioEntry.COUNTRY,
//                BioInfoContract.BioEntry.HEIGHT,
//                BioInfoContract.BioEntry.WEIGHT,
//                BioInfoContract.BioEntry.SEX,
//                BioInfoContract.BioEntry.AGE,
//                BioInfoContract.BioEntry.IMG_PATH
//        };
//
//        String selection = BioInfoContract.BioEntry.IS_LOGGED_IN + " = 1";
//
//        // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                BioInfoContract.BioEntry.USER_NAME + " ASC";
//
//        Cursor cursor = db.query(
//                BioInfoContract.BioEntry.TABLE_NAME,   // The table to query
//                projection,             // The array of columns to return (pass null to get all)
//                selection,              // The columns for the WHERE clause
//                null,          // The values for the WHERE clause
//                null,                   // don't group the rows
//                null,                   // don't filter by row groups
//                sortOrder               // The sort order
//        );
//
//        if(cursor != null) {
//            int rowID, age, weight;
//            String username, city, country, height, weight, sex, imgPath;
//            if (cursor.moveToFirst()) {
//                rowID = cursor.getInt(0);
//                username = cursor.getString(1);
//                city = cursor.getString(2);
//                country = cursor.getString(3);
//                height = cursor.getString(4);
//                weight = cursor.getString(5);
//                sex = cursor.getString(6);
//                age = cursor.getString(7);
//                imgPath = cursor.getString(8);
//            }
//            cursor.close();
//            db.close();
//            Intent bioIntent = new Intent(this, BioActivity.class);
//            Bundle bioBndl = new Bundle();
//            bioBndl.putString("rowID", rowID);
//            bioIntent.putExtras(bioBndl);
//            startActivity(bioIntent);
//        } else {
//            return null;
//        }
//    }

//    private void startBioActivity() {
//        String rowID = getRowID();
//        if (rowID != null) {
//            Intent bioIntent = new Intent(this, BioActivity.class);
//            Bundle bioBndl = new Bundle();
//            bioBndl.putString("rowID", rowID);
//            bioIntent.putExtras(bioBndl);
//            startActivity(bioIntent);
//        } else {
//            Intent bioEditIntent = new Intent(this, BioEditActivity.class);
//            startActivity(bioEditIntent);
//        }
//    }
}
