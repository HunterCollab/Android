package com.example.socialmediaapp.loopjtasks;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.*;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class MyLoopjTask {

    private static final String TAG = "O";
    private final Context context;
    private final OnLoopComplete loopjListener;

    AsyncHttpClient asyncHttpClient;
    RequestParams requestParams;

    String BASE_URL = "https://huntercollabapi.herokuapp.com/user/createUser";
    String jsonResponse;

    public MyLoopjTask(Context context, OnLoopComplete listener) {
        asyncHttpClient = new AsyncHttpClient();
        requestParams = new RequestParams();
        this.context = context;
        this.loopjListener = listener;
    }

    public void executeLoopjCall(String queryTerm1, String queryTerm2){
        requestParams.put("username", queryTerm1);
        requestParams.put("password", queryTerm2);

        asyncHttpClient.get(BASE_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                jsonResponse = response.toString();
                loopjListener.taskCompleted((jsonResponse));
                Log.i(TAG, "onSuccess" + jsonResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(TAG, "onFailure" + throwable);
            }
        });
    }

    public interface OnLoopComplete {
        public void  taskCompleted(String result);
    }
}

