package com.example.lab5;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.zip.Inflater;


public class ListFrag extends Fragment {
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    MyRVAdaptor mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,container,false);

        //Get the recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_Master);

        //Tell Android that we know the size of the recyclerview
        //doesn't change
        mRecyclerView.setHasFixedSize(true);

        //Set the layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        ArrayList<String> inputData = getArguments().getStringArrayList("listData");
        //Set the adapter
        mAdapter = new MyRVAdaptor(inputData);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
