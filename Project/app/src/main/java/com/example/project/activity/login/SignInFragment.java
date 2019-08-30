package com.example.project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.project.activity.login.SignUpActivity;

public class SignInFragment extends DialogFragment implements View.OnClickListener{
    private EditText signInET, signUpET;
    private LayoutInflater inflater;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        inflater = requireActivity().getLayoutInflater();
        View signInView = inflater.inflate(R.layout.sign_in_fragment, null);

        Button signInBtn = signInView.findViewById(R.id.signInBtn);
        Button signUpBtn = signInView.findViewById(R.id.signUpBtn);
        signInBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(signInView);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.signInBtn:
                Toast toast = Toast.makeText(getContext(), "Signing In..." , Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.signUpBtn:
                Intent signUpPage = new Intent(getContext(), SignUpActivity.class);
                startActivity(signUpPage);
                dismiss();
        }
    }
}
