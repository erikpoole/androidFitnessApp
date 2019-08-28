package com.example.lab2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class TopFragment extends Fragment implements View.OnClickListener {

    TopInterface dataPasser;
    View mView;

    @Override
    public void onClick(View v) {
        EditText ourText = mView.findViewById(R.id.name);
        dataPasser.handleTransaction(ourText.getText().toString());
    }

    public interface TopInterface {
        void handleTransaction(String fullName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_top, container, false);
        mView = view;


        Button theButton = (Button) view.findViewById(R.id.button1);
        theButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TopInterface) {
            dataPasser = (TopInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement 'Top Interface'");
        }
    }

}
