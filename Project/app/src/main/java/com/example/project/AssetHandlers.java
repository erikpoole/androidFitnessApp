package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.database.UserProfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AssetHandlers {

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
    public static void loadProfileImage(Activity activity, Menu menu, UserProfile userProfile) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        ImageView drawerImg = activity.findViewById(R.id.nav_header_imageView);
        TextView drawerTv = activity.findViewById(R.id.nav_header_textView);

        File sd = Environment.getExternalStorageDirectory();
        String imgPath = userProfile.getImgPath();
        Log.d("image", "onCreateOptionsMenu: " + imgPath);
        if (imgPath != null) {
            File imgFile = new File(userProfile.getImgPath());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                drawerImg.setImageBitmap(myBitmap);
                drawerTv.setText(userProfile.getName());
                drawerTv.setTextColor(Color.WHITE);
            }
        }
    }
}
