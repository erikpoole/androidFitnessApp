package com.example.project;

import android.content.Context;

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
}
