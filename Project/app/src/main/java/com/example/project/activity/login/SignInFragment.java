package com.example.project.activity.login;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.database.UserProfile;

public class SignInFragment extends DialogFragment implements View.OnClickListener {
    private EditText nameET, passwordET;
    private TextView errTV;
    private View vw;
    private Context ctx;
    private inputPersist persist;

    public interface inputPersist {
        void onSaveSnapshot(String name, String password);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View signInView = inflater.inflate(R.layout.sign_in_fragment, null);
        vw = signInView;

        nameET = signInView.findViewById(R.id.username_login);
        passwordET = signInView.findViewById(R.id.password_login);
        errTV = signInView.findViewById(R.id.err_login);

        nameET.setText(getArguments().getString("name"));
        passwordET.setText(getArguments().getString("password"));

        Button signInBtn = signInView.findViewById(R.id.signInBtn);
        Button signUpBtn = signInView.findViewById(R.id.signUpBtn);
        signInBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

        ctx = signInView.getContext();

        builder.setView(signInView);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.signInBtn:
                String name = nameET.getText().toString();
                String password = passwordET.getText().toString();
                UserProfile userProfile = new UserProfile(ctx);
                if (userProfile.login(name, password)) {
                    Intent main = new Intent(ctx, MainActivity.class);
                    startActivity(main);
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

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        try {
            persist = (inputPersist) ctx;
        } catch (ClassCastException e) {
            throw new ClassCastException(ctx.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle sis) {
        super.onSaveInstanceState(sis);
        persist.onSaveSnapshot(nameET.getText().toString(), passwordET.getText().toString());
    }
}
