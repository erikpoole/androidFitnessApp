package com.example.project.activity.bio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class BioActivity extends AppCompatActivity {

    BioHelperDB dbHelper;
    TextView nameTV, ageTV, sexTV, heightTV, weightTV, cityTV, countryTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);
        nameTV = findViewById(R.id.bio_name);
        ageTV = findViewById(R.id.bio_age);
        sexTV = findViewById(R.id.bio_sex);
        heightTV = findViewById(R.id.bio_height);
        weightTV = findViewById(R.id.bio_weight);
        cityTV = findViewById(R.id.bio_city);
        countryTV = findViewById(R.id.bio_country);

        Button editBioBtn = findViewById(R.id.edit_bio_btn);
        editBioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = view.getContext();
                Toast toast = Toast.makeText(ctx, "Start Bio Activity", Toast.LENGTH_SHORT);
                toast.show();
//                Intent editBioPage = new Intent(ctx, BioEditActivity.class);
//                startActivity(editBioPage);
            }
        });

        dbHelper = new BioHelperDB(getApplicationContext());
        getInfoFromDB();
    }

    private void getInfoFromDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BioInfoContract.BioEntry.USER_NAME,
                BioInfoContract.BioEntry.CITY,
                BioInfoContract.BioEntry.COUNTRY,
                BioInfoContract.BioEntry.HEIGHT,
                BioInfoContract.BioEntry.WEIGHT,
                BioInfoContract.BioEntry.AGE,
                BioInfoContract.BioEntry.SEX,
        };
        String selection = BioInfoContract.BioEntry.IS_LOGGED_IN + " = ?";
        String[] selectionArgs = { "1" };

        String sortOrder =
                BioInfoContract.BioEntry.USER_NAME + " ASC";

        Cursor cursor = db.query(
                BioInfoContract.BioEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            nameTV.setText(cursor.getString(0));
            cityTV.setText(cursor.getString(1));
            countryTV.setText(cursor.getString(2));
            heightTV.setText(cursor.getString(3));
            weightTV.setText(cursor.getString(4));
            ageTV.setText(cursor.getString(5));
            sexTV.setText(cursor.getString(6));
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_SHORT);
            toast.show();
        }

        cursor.close();
        db.close();
    }
}
