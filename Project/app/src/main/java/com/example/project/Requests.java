package com.example.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Requests {

    /*
    inputURL - url to request
    listener - listener with implementation on how to handle response to your request
    context - in almost all cases, pass 'this' from your activity
     */
    public static void makeStringRequest(String inputURL, Response.Listener<String> listener, Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                inputURL,
                listener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR: ", "makeStringRequest error - " + error.toString());
                    }
                });

        queue.add(request);
    }

    //TODO: not currently being used - remove by end of project if unimportant
    public static void makeImageRequest(String inputURL, Response.Listener<Bitmap> listener, Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        ImageRequest request = new ImageRequest(
                inputURL,
                listener,
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR: ", "makeImageRequest error - " + error.toString());
            }
        });

        queue.add(request);
    }
}
