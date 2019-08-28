package com.example.project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BioEdit extends AppCompatActivity implements View.OnClickListener {

    BioHelperDB dbHelper;
    EditText nameET, ageET, sexET, heightET, weightET, cityET, countryET;
    String username, age, sex, height, weight, city, country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_edit);
        nameET = findViewById(R.id.name_edit);
        ageET = findViewById(R.id.age_edit);
        sexET = findViewById(R.id.sex_edit);
        heightET = findViewById(R.id.height_edit);
        weightET = findViewById(R.id.weight_edit);
        cityET = findViewById(R.id.city_edit);
        countryET = findViewById(R.id.country_edit);

        Button submitBtn = findViewById(R.id.submit_edit);
        submitBtn.setOnClickListener(this);
        Button testBtn = findViewById(R.id.test_edit);
        testBtn.setOnClickListener(this);

        dbHelper = new BioHelperDB(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.submit_edit:
                assignVariablesToFieldVals();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(BioInfoContract.BioEntry.USER_NAME, username);
                values.put(BioInfoContract.BioEntry.AGE, age);
                values.put(BioInfoContract.BioEntry.SEX, sex);
                values.put(BioInfoContract.BioEntry.HEIGHT, height);
                values.put(BioInfoContract.BioEntry.WEIGHT, weight);
                values.put(BioInfoContract.BioEntry.CITY, city);
                values.put(BioInfoContract.BioEntry.COUNTRY, country);
                values.put(BioInfoContract.BioEntry.IMG_PATH, "");
                values.put(BioInfoContract.BioEntry.COUNTRY, country);
                long newRowId = db.insert(BioInfoContract.BioEntry.TABLE_NAME, null, values);
                db.close();
        }

    }

    private void assignVariablesToFieldVals() {
        username = nameET.getText().toString();
        age = ageET.getText().toString();
        sex = sexET.getText().toString();
        height = heightET.getText().toString();
        weight = weightET.getText().toString();
        city = cityET.getText().toString();
        country = countryET.getText().toString();
    }

    private void getInfoFromDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // COMMENTS FROM SQLite ANDROID DOCS
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                BioInfoContract.BioEntry.USER_NAME,
                BioInfoContract.BioEntry.CITY,
                BioInfoContract.BioEntry.COUNTRY,
                BioInfoContract.BioEntry.HEIGHT,
                BioInfoContract.BioEntry.WEIGHT,
                BioInfoContract.BioEntry.AGE,
                BioInfoContract.BioEntry.SEX,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = BioInfoContract.BioEntry.USER_NAME + " = ?";
        String[] selectionArgs = { "Jordan Higgins" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                BioInfoContract.BioEntry.USER_NAME + " ASC";

        Cursor cursor = db.query(
                BioInfoContract.BioEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                String id = cursor.getString(0);
                username = cursor.getString(1);
                city = cursor.getString(2);
                country = cursor.getString(3);
                height = cursor.getString(4);
                weight = cursor.getString(5);
                age = cursor.getString(6);
                sex = cursor.getString(7);
            }
        } else {
            username = "lame...";
        }

        cursor.close();
        db.close();
    }

    private void deleteUserFromDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = BioInfoContract.BioEntry.USER_NAME + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { "Jordan Higgins" };
        // Issue SQL statement.
        int deletedRows = db.delete(BioInfoContract.BioEntry.TABLE_NAME, selection, selectionArgs);
    }

    private Boolean updateUserBio(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // New value for one column
        String ID = "MyNewTitle";
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, title);

        // Which row to update, based on the title
        String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = { "MyOldTitle" };

        int count = db.update(
                FeedReaderDbHelper.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}
