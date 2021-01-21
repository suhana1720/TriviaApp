package com.example.triviaapp.controller;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Collection;

public class AppController extends Application {
   // in super class we use application bcus we want to override mth.
    public static final String TAG = AppController.class //for logging things
           .getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized AppController getInstance(){

        return mInstance;
    }

    @Override
    public void onCreate() { //getting instance save in memory
        super.onCreate();
        mInstance=this; // means my instance is equal to this instance
    }

    public RequestQueue getmRequestQueue(){
        if(mRequestQueue==null){
            mRequestQueue= Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;



    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        //set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getmRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag){
        if (mRequestQueue !=null){
            mRequestQueue.cancelAll(tag);
        }


// this class going to govered over entire project as we write it in mmenifest


}



}
