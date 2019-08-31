package com.example.project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.project.activity.MainActivity;
import com.example.project.activity.bio.BioHelperDB;
import com.example.project.activity.bio.BioInfoContract;
import com.example.project.activity.login.SignUpActivity;

public class SignInFragment extends DialogFragment implements View.OnClickListener{
    private EditText nameET, passwordET;
    private TextView errTV;
    private BioHelperDB dbHelper;
    private Context ctx;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View signInView = inflater.inflate(R.layout.sign_in_fragment, null);

        nameET = signInView.findViewById(R.id.username_login);
        passwordET = signInView.findViewById(R.id.password_login);
        errTV = signInView.findViewById(R.id.err_login);

        Button signInBtn = signInView.findViewById(R.id.signInBtn);
        Button signUpBtn = signInView.findViewById(R.id.signUpBtn);
        signInBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

        ctx = signInView.getContext();
        dbHelper = new BioHelperDB(ctx);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(signInView);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.signInBtn:
                String name = nameET.getText().toString();
                String password = passwordET.getText().toString();
                if (loginUser(name, password)) {
                    Intent mainPage = new Intent(ctx, MainActivity.class);
                    startActivity(mainPage);
                    dismiss();
                } else {
                    nameET.setText("");
                    passwordET.setText("");
                    errTV.setText("No user with that name and password...");
                }
                break;
            case R.id.signUpBtn:
                Intent signUpPage = new Intent(ctx, SignUpActivity.class);
                startActivity(signUpPage);
                dismiss();
        }
    }

    private Boolean loginUser(String name, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String isLoggedIn = "1";
        ContentValues values = new ContentValues();
        values.put(BioInfoContract.BioEntry.IS_LOGGED_IN, isLoggedIn);

        String selection = BioInfoContract.BioEntry.USER_NAME + " LIKE ? AND " + BioInfoContract.BioEntry.PASSWORD + " Like ?";
        String[] selectionArgs = { name, password };

        int count = db.update(
                BioInfoContract.BioEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (count < 1) {
            return false;
        }
        return true;
    }
}
