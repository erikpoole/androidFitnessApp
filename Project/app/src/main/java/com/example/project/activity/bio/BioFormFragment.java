package com.example.project.activity.bio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BioFormFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String PATH_TO_IMAGE;
    EditText ageET, sexET, cityET, countryET, heightET, weightET;
    ImageView profileIV;
    TextView weightTV;
    onSubmitFormListener submitListener;
    Context ctx;

    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.bio_form_fragment, vg,false);
        ctx = view.getContext();

//        ageET = view.findViewById(R.id.bio_form_age);
//        sexET = view.findViewById(R.id.bio_form_sex);
        cityET = view.findViewById(R.id.bio_form_city);
        countryET = view.findViewById(R.id.bio_form_country);
//        heightET = view.findViewById(R.id.bio_form_height);
//        weightET = view.findViewById(R.id.bio_form_weight);

        profileIV = view.findViewById(R.id.bio_form_img);

//        final Spinner ageSpinner = view.findViewById(R.id.bio_form_age);
//        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(ctx,
//                R.array.age_array, android.R.layout.simple_spinner_item);
//        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        ageSpinner.setAdapter(ageAdapter);
//        ageSpinner.setOnItemSelectedListener(this);


        final Spinner sexSpinner = view.findViewById(R.id.bio_form_sex);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(adapter);
        sexSpinner.setOnItemSelectedListener(this);

        final Spinner feetSpinner = view.findViewById(R.id.bio_form_ft);
        ArrayAdapter<CharSequence> feetAdapter = ArrayAdapter.createFromResource(ctx,
                R.array.feet_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feetSpinner.setAdapter(feetAdapter);
        feetSpinner.setOnItemSelectedListener(this);

        final Spinner inchSpinner = view.findViewById(R.id.bio_form_inch);
        ArrayAdapter<CharSequence> inchAdapter = ArrayAdapter.createFromResource(ctx,
                R.array.inch_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchSpinner.setAdapter(inchAdapter);
        inchSpinner.setOnItemSelectedListener(this);

        // set a change listener on the SeekBar
        final SeekBar seekBar = view.findViewById(R.id.bio_form_weight);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = seekBar.getProgress();
        weightTV = view.findViewById(R.id.bio_form_weight_label);
        weightTV.setText("Weight: " + progress);

        Button submitBtn = view.findViewById(R.id.bio_form_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String height = feetSpinner.getSelectedItem().toString() + " " + inchSpinner.getSelectedItem().toString();
                Toast.makeText(ctx, height, Toast.LENGTH_LONG).show();
                submitListener.onSubmitForm(
                    "12",
                    sexSpinner.getSelectedItem().toString(),
                    cityET.getText().toString(),
                    countryET.getText().toString(),
            feetSpinner.getSelectedItem().toString() + " " + inchSpinner.getSelectedItem().toString(),
                    seekBar.getProgress(),
                    PATH_TO_IMAGE
                );
            }
        });

        Button uploadImgBtn = view.findViewById(R.id.bio_form_upload_img);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(ctx.getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
            }
        });

        return view;
    }

    public interface onSubmitFormListener {
        void onSubmitForm(String age, String sex, String city, String country, String height, int weight, String imgPath);
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

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
         parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // TODO:
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            weightTV.setText("Weight: " + progress + " lbs.");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap thumbnailImage = (Bitmap) extras.get("data");
            if (isExternalStorageWritable()) {
                PATH_TO_IMAGE = saveImage(thumbnailImage);
                profileIV.setImageBitmap(thumbnailImage);
            }
        }
    }

    private String saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        if (myDir.mkdirs()) {
            Log.d("saveImage", "saveImage: dirs CREATED");
        } else {
            Toast.makeText(ctx, "mkdirs failed!", Toast.LENGTH_SHORT).show();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fname = "Thumbnail" + timeStamp + ".jpg";

        File file = new File(myDir, fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(ctx, "image saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    private boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

}
