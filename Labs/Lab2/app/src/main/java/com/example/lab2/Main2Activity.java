package com.example.lab2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

//public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
public class Main2Activity extends AppCompatActivity {
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bundle = getIntent().getExtras();
        String fullName = bundle.getString("userInput");
        String[] nameSplit = fullName.split(" ");

        TextView first = findViewById(R.id.text3);
        TextView last = findViewById(R.id.text4);

        first.setText(nameSplit[0]);
        last.setText(nameSplit[1]);

        String path = bundle.getString("imagePath");
        if (path!=null) {
            Bitmap thumbnail = BitmapFactory.decodeFile(path);
            ImageView imageHolder = findViewById(R.id.image);
            imageHolder.setImageBitmap(thumbnail);
        }



    }

}
