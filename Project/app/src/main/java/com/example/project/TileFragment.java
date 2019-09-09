package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.activity.HikingActivity;
import com.example.project.activity.MainActivity;
import com.example.project.activity.Weather.WeatherActivity;
import com.example.project.activity.bio.BioActivity;
import com.example.project.activity.bio.BmiActivity;
import com.example.project.activity.bio.CalorieActivity;

/*
 * This class was based on a demo class from the Android Design Library.
 */
public class TileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_tile, parent, false));
            picture = itemView.findViewById(R.id.tile_picture);
            name = itemView.findViewById(R.id.tile_title);
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private static final int NUM_TILES_TO_DISPLAY = 6;
        private final String[] mPlaces;
        private final Drawable[] mPlacePictures;

        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mPlaces = resources.getStringArray(R.array.activities);
            TypedArray a = resources.obtainTypedArray(R.array.activity_pictures);
            mPlacePictures = new Drawable[a.length()];
            for (int i = 0; i < mPlacePictures.length; i++) {
                mPlacePictures[i] = a.getDrawable(i);
            }
            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.picture.setImageDrawable(mPlacePictures[position % mPlacePictures.length]);
            holder.name.setText(mPlaces[position % mPlaces.length]);

            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (position) {
                        case 0:
                            Intent profilePage = new Intent(view.getContext(), BioActivity.class);
                            view.getContext().startActivity(profilePage);
                            return;
                        case 1:
                            Intent weatherPage = new Intent(view.getContext(), WeatherActivity.class);
                            view.getContext().startActivity(weatherPage);
                            return;
                        case 2:
                            Intent hikingPage = new Intent(view.getContext(), HikingActivity.class);
                            view.getContext().startActivity(hikingPage);
                            return;
                        case 3:
                            Intent bmiPage = new Intent(view.getContext(), BmiActivity.class);
                            view.getContext().startActivity(bmiPage);
                            return;
                        case 4:
                            Intent caloriePage = new Intent(view.getContext(), CalorieActivity.class);
                            view.getContext().startActivity(caloriePage);
                            return;
                        case 5:
                            String url = "https://www.cdc.gov/healthyweight/tools/index.html";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            view.getContext().startActivity(i);
                            return;
                        default:
                            return;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return NUM_TILES_TO_DISPLAY;
        }
    }
}