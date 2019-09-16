package com.example.project.activity.Weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project.AssetHandlers;
import com.example.project.R;
import com.github.pwittchen.weathericonview.WeatherIconView;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherFragment extends Fragment {
    private JSONObject weatherIcons;

    private WeatherIconView icon;
    private TextView maxTempText;
    private TextView minTempText;

    private String size;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        size = getArguments().getString("size");

        final View view = inflater.inflate(R.layout.fragment_weather, container, false);

        icon = view.findViewById(R.id.icon);
        maxTempText = view.findViewById(R.id.maxTempText);
        minTempText = view.findViewById(R.id.minTempText);

        try {
            weatherIcons = new JSONObject(AssetHandlers.readAsset("weatherIcons.json", getActivity()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonRaw = getArguments().getString("json");
        JSONObject json = null;

        try {
            json = new JSONObject(jsonRaw);
            maxTempText.setText(roundStringWithMultiplier(json.getString("temperatureHigh"), 1));
            minTempText.setText(roundStringWithMultiplier(json.getString("temperatureLow"), 1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //custom conversion to interact with WeatherIconView library (100 is default)
        if (size == "small") {
            icon.setIconSize(55);
        }
        if (size == "large") {
            icon.setIconSize(165);
            minTempText.setTextSize(48);
            maxTempText.setTextSize(48);
        }

        icon.setIconResource(getIconCode(json));

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

    public static String roundStringWithMultiplier(String input, int multiplier) {
        return String.valueOf(Math.round(Double.parseDouble(input) * multiplier));
    }

}
