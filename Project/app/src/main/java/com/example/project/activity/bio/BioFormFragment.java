package com.example.project.activity.bio;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.project.R;

public class BioFormFragment extends Fragment {

    EditText ageET, sexET, cityET, countryET, heightET, weightET;
    onSubmitFormListener submitListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.bio_form_fragment, vg,false);

        ageET = view.findViewById(R.id.bio_form_age);
        sexET = view.findViewById(R.id.bio_form_sex);
        cityET = view.findViewById(R.id.bio_form_city);
        countryET = view.findViewById(R.id.bio_form_country);
        heightET = view.findViewById(R.id.bio_form_height);
        weightET = view.findViewById(R.id.bio_form_weight);

        Button submitBtn = view.findViewById(R.id.bio_form_submit);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submitListener.onSubmitForm(
                        ageET.getText().toString(),
                        sexET.getText().toString(),
                        cityET.getText().toString(),
                        countryET.getText().toString(),
                        heightET.getText().toString(),
                        weightET.getText().toString()
                );
            }
        });
        return view;
    }

    public interface onSubmitFormListener {
        public void onSubmitForm(String age, String sex, String city, String country, String height, String weight);
    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);

        try {
            submitListener = (onSubmitFormListener) ctx;
        } catch (ClassCastException e) {
            throw new ClassCastException(ctx.toString() + " must implement OnArticleSelectedListener");
        }

    }

}
