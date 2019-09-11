package com.example.project.activity.bio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.database.UserProfile;

import java.io.File;

public class BioActivity extends AppCompatActivity {

    UserProfile userProfile;
    TextView nameTV, ageTV, sexTV, heightTV, weightTV;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        userProfile = new UserProfile(getApplicationContext());
        nameTV = findViewById(R.id.bio_name);
        ageTV = findViewById(R.id.bio_age);
        sexTV = findViewById(R.id.bio_sex);
        heightTV = findViewById(R.id.bio_height);
        weightTV = findViewById(R.id.bio_weight);

        imageView = findViewById(R.id.bio_img);

        populateInfo();

        Button editBioBtn = findViewById(R.id.edit_bio_btn);
        editBioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = view.getContext();
//                Toast toast = Toast.makeText(ctx, "Start Bio Activity", Toast.LENGTH_SHORT);
//                toast.show();
                Intent editBioPage = new Intent(ctx, BioEditActivity.class);
                startActivity(editBioPage);
            }
        });
    }

    private void populateInfo() {
        nameTV.setText(userProfile.getName());
        heightTV.setText(userProfile.getHeight());
        weightTV.setText(userProfile.getWeight() + " lbs.");
        ageTV.setText(userProfile.getAge() + " yrs.");
        sexTV.setText(userProfile.getSex());

        String imgPath = userProfile.getImgPath();

        if (imgPath != null) {
            File imgFile = new File(userProfile.getImgPath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            } else {
                Toast.makeText(getApplicationContext(), "img path not found... " + userProfile.getImgPath(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
