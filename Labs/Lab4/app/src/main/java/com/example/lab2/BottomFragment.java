package com.example.lab2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


public class BottomFragment extends Fragment {

    private TextView mTvFirstName, mTvLastName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom, container, false);

        //Get the text views
        mTvFirstName = view.findViewById(R.id.text3);
        mTvLastName = view.findViewById(R.id.text4);

        //Get the data that was sent in
        String firstName = getArguments().getString("FN_DATA");
        String lastName = getArguments().getString("LN_DATA");

        //Set the data
        if (firstName != null) {
            mTvFirstName.setText("" + firstName);
        }
        if (lastName != null) {
            mTvLastName.setText("" + lastName);
        }
        return view;
    }
}
