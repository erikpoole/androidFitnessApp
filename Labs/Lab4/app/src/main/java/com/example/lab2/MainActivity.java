package com.example.lab2;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements TopFragment.TopInterface {

    private String mStringFullName;
    private EditText mEtFullName;
    private Button mBtSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get stuff
//        mEtFullName = (EditText) findViewById(R.id.et_name);
//        mBtSubmit = (Button) findViewById(R.id.button_submit);
//        mBtSubmit.setOnClickListener(this);

        BottomFragment viewFragment = new BottomFragment();

        //Send data to it
        Bundle sentData = new Bundle();
        sentData.putString("FN_DATA","Poop");
        sentData.putString("LN_DATA","Asdf");
        viewFragment.setArguments(sentData);

        //Find each frame layout, replace with corresponding fragment
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
//        fTrans.replace(R.id.fl_frag_ph_1,new Fragment1(),"Frag_1");
        fTrans.replace(R.id.fl_bottom, viewFragment, "Frag BottomFragment");
        fTrans.commit();
    }

    @Override
    public void handleTransaction(String name) {

    }

}
