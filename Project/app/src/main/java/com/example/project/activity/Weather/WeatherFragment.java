package com.example.project.activity.Weather;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.project.AssetHandlers;
import com.example.project.R;
import com.github.pwittchen.weathericonview.WeatherIconView;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherFragment extends Fragment {
    private JSONObject weatherIcons;
    private WeatherIconView icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_weather, container, false);

        icon = view.findViewById(R.id.icon);

        try {
            weatherIcons = new JSONObject(AssetHandlers.readAsset("weatherIcons.json", getActivity()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonRaw = getArguments().getString("json");
        JSONObject json = null;

        try {
            json = new JSONObject(jsonRaw);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        icon.setIconResource(getIconCode(json));
        icon.setIconSize(getArguments().getInt("size"));

        return view;
    }


    private int getStringIdentifier(String name) {
        return this.getResources().getIdentifier(name, "string", getActivity().getPackageName());
    }

    private String getIconCode(JSONObject json) {
        try {
            String rawIcon = json.getString("icon");
            String convertedIcon = weatherIcons.getString(rawIcon);
            return getString(getStringIdentifier(convertedIcon));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
