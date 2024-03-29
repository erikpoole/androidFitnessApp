package com.example.project.activity.bio;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.project.R;
import com.example.project.UserViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BioFormFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private UserViewModel userViewModel;
    final int MY_PERMISSIONS_REQUEST_STORAGE = 88;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String PATH_TO_IMAGE;
    ImageView profileIV;
    TextView weightTV;
    onSubmitFormListener submitListener;
    Context ctx;

    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.bio_form_fragment, vg,false);
        ctx = view.getContext();

        profileIV = view.findViewById(R.id.bio_form_img);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel = new UserViewModel(getActivity().getApplication());

        // UPDATING SEX
        final Spinner sexSpinner = view.findViewById(R.id.bio_form_sex);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(adapter);
        sexSpinner.setOnItemSelectedListener(this);

        final Observer<String> sexObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String sex) {
                // Update the UI, in this case, a TextView.
                if (sex != null) {
                    if (sex.equals("Non-binary")) {
                        sexSpinner.setSelection(2);
                    } else if (sex.equals("Female")) {
                        sexSpinner.setSelection(1);
                    }
                }
            }
        };

        userViewModel.getSex().observe(this, sexObserver);

        // UPDATING HEIGHT
        final Spinner feetSpinner = view.findViewById(R.id.bio_form_ft);
        ArrayAdapter<CharSequence> feetAdapter = ArrayAdapter.createFromResource(ctx,
                R.array.feet_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feetSpinner.setAdapter(feetAdapter);
        feetSpinner.setOnItemSelectedListener(this);
        feetSpinner.setSelection(2);

        final Spinner inchSpinner = view.findViewById(R.id.bio_form_inch);
        ArrayAdapter<CharSequence> inchAdapter = ArrayAdapter.createFromResource(ctx,
                R.array.inch_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchSpinner.setAdapter(inchAdapter);
        inchSpinner.setOnItemSelectedListener(this);

        final Observer<String> heightObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String height) {
                if (height != null) {
                    String heightAdj = height.substring(0, height.length() - 1);
                    String[] heightDimensions = heightAdj.split("'");
                    feetSpinner.setSelection(Integer.parseInt(heightDimensions[0]) - 3);
                    inchSpinner.setSelection(Integer.parseInt(heightDimensions[1]));
                }
            }
        };

        userViewModel.getHeight().observe(this, heightObserver);

        // UPDATING WEIGHT
        final SeekBar seekBar = view.findViewById(R.id.bio_form_weight);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        final Observer<Integer> weightObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer weight) {
                // Update the UI, in this case, a TextView.
                if (weight != null) {
                    seekBar.setProgress(weight);
                }
            }
        };

        userViewModel.getWeight().observe(this, weightObserver);
        int progress = seekBar.getProgress();
        weightTV = view.findViewById(R.id.bio_form_weight_label);
        weightTV.setText(" " + progress + " lbs.");


        // UPDATING IMG
        final Observer<String> imgPathObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String imgPath) {
                // Update the UI, in this case, a TextView.
                if (imgPath != null) {
                    PATH_TO_IMAGE = imgPath;
                    File imgFile = new File(imgPath);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        profileIV.setImageBitmap(myBitmap);
                    }
                }
            }
        };
        userViewModel.getImgPath().observe(this, imgPathObserver);


        // ONCLICK HANDLERS
        Button submitBtn = view.findViewById(R.id.bio_form_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String height = feetSpinner.getSelectedItem().toString() + " " + inchSpinner.getSelectedItem().toString();
                submitListener.onSubmitForm(
                        sexSpinner.getSelectedItem().toString(),
                        feetSpinner.getSelectedItem().toString() + "'" + inchSpinner.getSelectedItem().toString() + "\"",
                        seekBar.getProgress(),
                        PATH_TO_IMAGE
                );
            }
        });

        Button uploadImgBtn = view.findViewById(R.id.bio_form_upload_img);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                requestAppPermissions();
                if (hasReadPermissions() && hasWritePermissions()) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(ctx.getPackageManager()) != null) {
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else {
                    Toast.makeText(ctx, "storage permissions denied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public interface onSubmitFormListener {
        void onSubmitForm(String sex, String height, int weight, String imgPath);
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
            weightTV.setText(" " + progress + " lbs.");
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
//            Toast.makeText(ctx, "mkdirs failed!", Toast.LENGTH_SHORT).show();
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

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions((Activity) ctx,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, MY_PERMISSIONS_REQUEST_STORAGE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

}
