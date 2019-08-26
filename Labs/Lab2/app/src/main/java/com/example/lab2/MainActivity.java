package com.example.lab2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String PATH_TO_IMAGE;
    private String NAME;
    private EditText NAME_INPUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2 = findViewById(R.id.button_take_picture);
        button2.setOnClickListener(this);
        NAME_INPUT = findViewById(R.id.name);

        if (savedInstanceState != null) {
            PATH_TO_IMAGE = savedInstanceState.getString("imgPath");
            NAME = savedInstanceState.getString("fullname");
            NAME_INPUT.setText(NAME);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button1:
                Intent secondPage = new Intent(this, Main2Activity.class);
                Bundle nameBndl = new Bundle();
                NAME = NAME_INPUT.getText().toString();
                nameBndl.putString("userInput", NAME);
                nameBndl.putString("imagePath", PATH_TO_IMAGE);
                secondPage.putExtras(nameBndl);
                startActivity(secondPage);
                break;
            case R.id.button_take_picture:
                Toast.makeText(getApplicationContext(), "Case activated", Toast.LENGTH_SHORT).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap thumbnailImage = (Bitmap) extras.get("data");

            if (isExternalStorateWritable()) {
                PATH_TO_IMAGE = saveImage((thumbnailImage));
            }
        }
    }

    private String saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fname = "Thumbnail" + timeStamp + ".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(this, "file saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    private boolean isExternalStorateWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle sis) {
        super.onSaveInstanceState(sis);
        NAME = NAME_INPUT.getText().toString();
        sis.putString("fullname", NAME);
        sis.putString("imgPath", PATH_TO_IMAGE);
    }

//    @Override
//    public void onRestoreInstanceState(Bundle sis) {
//        super.onRestoreInstanceState(sis);
//        PATH_TO_IMAGE = sis.getString("imgPath");
//        NAME = sis.getString("fullname");
//        NAME_INPUT.setText(NAME);
//    }
}
