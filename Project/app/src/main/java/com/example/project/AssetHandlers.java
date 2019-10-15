package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.project.activity.BmiActivity;
import com.example.project.activity.CalorieActivity;
import com.example.project.activity.HikingActivity;
import com.example.project.activity.MainActivity;
import com.example.project.activity.Weather.WeatherActivity;
import com.example.project.activity.bio.BioActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class AssetHandlers {

    private static String profileImagePath;

    public static String readAsset(String filename, Context context) {
        try {
            InputStream iStream = context.getAssets().open(filename);
            int size = iStream.available();
            byte[] buffer = new byte[size];
            iStream.read(buffer);
            iStream.close();
            return new String(buffer);
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    //use in onCreateOptionsMenu
    public static void loadProfileImage(Activity activity, Menu menu, UserViewModel userViewModel) {
        userViewModel.getImgPath().observe((LifecycleOwner) activity, new Observer<String>() {
            @Override
            public void onChanged(String imgPath) {
                if (imgPath != null) {
                    profileImagePath = imgPath;
                }
            }
        });

        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        ImageView drawerImg = activity.findViewById(R.id.nav_header_imageView);
        TextView drawerTv = activity.findViewById(R.id.nav_header_textView);

        File sd = Environment.getExternalStorageDirectory();
        if (profileImagePath != null) {
            File imgFile = new File(profileImagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                drawerImg.setImageBitmap(myBitmap);
                drawerTv.setText(userViewModel.getName().getValue());
                drawerTv.setTextColor(Color.WHITE);
            }
        }
    }

    //used in drawer
    public static boolean handleNavigationEvent(Activity activity, @NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_home:
                Intent mainPage = new Intent(activity.getApplicationContext(), MainActivity.class);
                activity.startActivity(mainPage);
                return true;
            case R.id.nav_weather:
                Intent weatherPage = new Intent(activity.getApplicationContext(), WeatherActivity.class);
                activity.startActivity(weatherPage);
                return true;
            case R.id.nav_hiking:
                Intent hikingPage = new Intent(activity.getApplicationContext(), HikingActivity.class);
                activity.startActivity(hikingPage);
                return true;
            case R.id.nav_bio:
                Intent bioPage = new Intent(activity.getApplicationContext(), BioActivity.class);
                activity.startActivity(bioPage);
                return true;
            case R.id.nav_bmi:
                Intent bmiPage = new Intent(activity.getApplicationContext(), BmiActivity.class);
                activity.startActivity(bmiPage);
                return true;
            case R.id.nav_calorie:
                Intent caloriePage = new Intent(activity.getApplicationContext(), CalorieActivity.class);
                activity.startActivity(caloriePage);
                return true;
            default:
                return false;
        }
    }
}
