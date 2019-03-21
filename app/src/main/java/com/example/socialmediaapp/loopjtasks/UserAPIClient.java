package com.example.socialmediaapp.loopjtasks;

import android.content.Context;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;


import cz.msebera.android.httpclient.Header;

public class UserAPIClient {

    private final Context context;
    private final OnRetrieveDetails retrieveDetailsListener;

    public UserAPIClient(Context context, OnRetrieveDetails listener){
        this.context = context;
        this.retrieveDetailsListener = listener;
    }

    public void getUserDetails(){
        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);

        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/user", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                retrieveDetailsListener.detailsRetrieved(true, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                retrieveDetailsListener.detailsRetrieved(false, null);
            }
        });
    }

    // interface
    public interface OnRetrieveDetails {
        public void detailsRetrieved(Boolean success, JSONObject resp);
    }
}
