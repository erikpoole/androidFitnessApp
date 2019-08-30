package com.example.project.activity.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.activity.bio.BioFormFragment;
import com.example.project.activity.bio.BioHelperDB;
import com.example.project.activity.bio.BioInfoContract;

public class SignUpActivity extends AppCompatActivity implements BioFormFragment.onSubmitFormListener {

    EditText nameET, psswdET, psswdConfirmET;
    BioHelperDB dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new BioHelperDB(getApplicationContext());

        nameET = findViewById(R.id.name_sign_up);
        psswdET = findViewById(R.id.password_sign_up);
        psswdConfirmET = findViewById(R.id.confirm_password_sign_up);
    }

    public void onSubmitForm(String age, String sex, String city, String country, String height, String weight) {
        String name = nameET.getText().toString();
        String password = psswdET.getText().toString();
        String confirmPassword = psswdConfirmET.getText().toString();
        int weightAsInt = 0;
        if (!weight.equals("")) {
            weightAsInt = Integer.parseInt(weight);
        }

        if (name.equals("") || password.equals("") || confirmPassword.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please fill out all required fields", Toast.LENGTH_SHORT);
            toast.show();
        } else if (!password.equals(confirmPassword)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(BioInfoContract.BioEntry.USER_NAME, name);
            values.put(BioInfoContract.BioEntry.PASSWORD, password);
            values.put(BioInfoContract.BioEntry.AGE, age);
            values.put(BioInfoContract.BioEntry.SEX, sex);
            values.put(BioInfoContract.BioEntry.CITY, city);
            values.put(BioInfoContract.BioEntry.COUNTRY, country);
            values.put(BioInfoContract.BioEntry.HEIGHT, height);
            values.put(BioInfoContract.BioEntry.WEIGHT, weightAsInt);
            values.put(BioInfoContract.BioEntry.IS_LOGGED_IN, 1);

            long newRowId = db.insert(BioInfoContract.BioEntry.TABLE_NAME, null, values);
            db.close();
            if (newRowId < 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
            }
        }
    }
}
