package com.example.project.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.activity.bio.BioFormFragment;
import com.example.project.activity.bio.DatePicker;
import com.example.project.database.UserHelper;
import com.example.project.database.UserProfile;

public class SignUpActivity extends AppCompatActivity implements BioFormFragment.onSubmitFormListener, DatePicker.onDateSetListener {

    EditText nameET, psswdET, psswdConfirmET;
    TextView ageTV;
    UserHelper dbHelper;
    UserProfile userProfile;
    Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new UserHelper(getApplicationContext());
        ctx = getApplicationContext();

        ageTV = findViewById(R.id.bio_form_age);
        ageTV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        nameET = findViewById(R.id.name_sign_up);
        psswdET = findViewById(R.id.password_sign_up);
        psswdConfirmET = findViewById(R.id.confirm_password_sign_up);
    }

    // Fragment onSubmitForm interface method definition
    public void onSubmitForm(String age, String sex, String city, String country, String height, int weight, String imgPath) {
        String name = nameET.getText().toString();
        String password = psswdET.getText().toString();
        String confirmPassword = psswdConfirmET.getText().toString();
        if (name.equals("") || password.equals("") || confirmPassword.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please fill out all required fields", Toast.LENGTH_SHORT);
            toast.show();
        } else if (!password.equals(confirmPassword)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            userProfile = new UserProfile(ctx);
            userProfile.setName(name);
            userProfile.setAge(age);
            userProfile.setWeight(weight);
            userProfile.setHeight(height);
            userProfile.setSex(sex);
            userProfile.setCity(city);
            userProfile.setCountry(country);
            userProfile.setImgPath(imgPath);
            if (!userProfile.signUp(password)) {
                Toast toast = Toast.makeText(ctx, "Something went wrong...", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePicker();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onDateSetEvent(int year, int month, int day) {
        Toast.makeText(ctx, "Date: " + year + ", " + month + ", " + day, Toast.LENGTH_SHORT).show();
        ageTV.setText("Date" + year + ", " + month + ", " + day);
    }
}
