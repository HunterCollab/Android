package com.example.socialmediaapp.loopjtasks;

import android.content.Context;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class DoSearch {

    private final Context context;
    private final OnDoSearchComplete searchCompleteListener;

    public DoSearch(Context context, OnDoSearchComplete listener) {
        this.context = context;
        this.searchCompleteListener = listener;
    }

    public void doSearch(String search, String constraint){

        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);
        final RequestParams requestParams = new RequestParams();
        requestParams.put("query", constraint);
        System.out.println("URL: " + GlobalConfig.BASE_API_URL + "/search/skills" + requestParams);
        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/search/skills", requestParams, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response.has("matches") && response.isNull("matches") == false) {


                    JSONArray terms = null;
                    try {
                        terms = response.getJSONArray("matches");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    searchCompleteListener.searchCompleted(true, terms);

                } else {
                    searchCompleteListener.searchCompleted(true, null);
                }
            }
        });

    }


    public interface OnDoSearchComplete {

        public void searchCompleted(Boolean success, JSONArray message);

    }
}
