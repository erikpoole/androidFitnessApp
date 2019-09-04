package com.example.lab5;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

        //Get the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_Master);

        //Tell Android that we know the size of the recyclerview
        //doesn't change
        mRecyclerView.setHasFixedSize(true);

        //Set the layout manager
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //Populate the list with data
        List<String> inputList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            inputList.add("Item " + i);
        }

        //Set the adapter
        mAdapter = new MyRVAdaptor(inputList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
