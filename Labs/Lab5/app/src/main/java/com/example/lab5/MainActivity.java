package com.example.lab5;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Populate the list with data
        ArrayList<String> inputList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            inputList.add("Item " + i);
        }

        // pass list as bundle
        ListFrag fragment = new ListFrag();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("listData", inputList);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_temp, fragment);
        transaction.commit();
    }
}
