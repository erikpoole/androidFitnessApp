package com.example.lab2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        TopFragment topFragment = new TopFragment();

        //Find each frame layout, replace with corresponding fragment
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
//        fTrans.replace(R.id.fl_frag_ph_1,new Fragment1(),"Frag_1");
        fTrans.replace(R.id.fl_top, topFragment, "Frag TopFragment");
        fTrans.commit();

    }

    @Override
    public void handleTransaction(String name) {
        String[] names = name.split(" ");
        BottomFragment viewFragment = new BottomFragment();

        //Send data to it
        Bundle sentData = new Bundle();
        sentData.putString("FN_DATA", names[0]);
        sentData.putString("LN_DATA", names[1]);
        viewFragment.setArguments(sentData);

        //Find each frame layout, replace with corresponding fragment
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
//        fTrans.replace(R.id.fl_frag_ph_1,new Fragment1(),"Frag_1");
        fTrans.replace(R.id.fl_bottom, viewFragment, "Frag BottomFragment");
        fTrans.commit();
    }

}
