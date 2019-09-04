package com.example.lab5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRVAdaptor extends RecyclerView.Adapter<MyRVAdaptor.ViewHolder> {
    private List<String> mListItems;
    private Context mContext;

    public MyRVAdaptor(List<String> inputList) {
        mListItems = inputList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected View itemLayout;
        protected TextView itemTvData;

        public ViewHolder(View view) {
            super(view);
            itemLayout = view;
            itemTvData = (TextView) view.findViewById(R.id.tv_data);
        }
    }

    @NonNull
    @Override
    public MyRVAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View myView = layoutInflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.itemTvData.setText(mListItems.get(position));
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     TextView data = view.findViewById(R.id.tv_data);
                     String dataStr = data.getText().toString();
                     Toast.makeText(mContext, "Item " + dataStr, Toast.LENGTH_SHORT).show();
//                     remove(position); // Varun said to remove this
                 }
             }
        );
    }

    public void remove(int position) {
        mListItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

}