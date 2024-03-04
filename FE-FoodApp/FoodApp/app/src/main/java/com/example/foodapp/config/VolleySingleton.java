package com.example.foodapp.config;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static final String TAG = "volleySingleton";
    private static Context mContext;
    private RequestQueue requestQueue;
    private static VolleySingleton instance;
    private VolleySingleton(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();
    }
    public static synchronized VolleySingleton getInstance(Context context) {
        if(instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }
    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
