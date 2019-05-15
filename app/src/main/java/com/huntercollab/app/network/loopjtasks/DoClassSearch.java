package com.huntercollab.app.network.loopjtasks;

import android.content.Context;

import com.huntercollab.app.config.GlobalConfig;
import com.huntercollab.app.utils.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DoClassSearch {

    private final OnDoClassSearchComplete searchListener;
    private Context context;
    private RequestParams requestParams;

    //@author: Edwin Quintuna
    //@brief: Constructor with a listener for the class search
    //@params: [Context context] [OnDoClassSearchComplete listener]
    public DoClassSearch(Context context, OnDoClassSearchComplete listener){
        this.context = context;
        this.searchListener = listener;
    }


    //AsyncHttpClient asyncHttpClient
    //@author: Edwin Quintuna
    //@brief:
    //ASYNC HTTP GET request, receives data in a JSON
    //Retrieves class data from the database that matches user input and returns the list of matched classes if successful
    //Empty list if not successful
    //@params: [String constraint]
    //@pre condition: Data set for autocomplete not matching user input
    //@post condition: Data set for autocomplete matches user input
    public void doClassSearch(String constraint){

        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);

        requestParams = new RequestParams();
        requestParams.put("query", constraint);

        asyncHttpClient.get
                (GlobalConfig.BASE_API_URL + "/search/classes",
                        requestParams, new JsonHttpResponseHandler(){

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);

                                ArrayList<String> stringList = new ArrayList<>();

                                System.out.println("Response: " + response);

                                try {
                                    JSONArray terms = null;
                                    terms = response.getJSONArray("matches");

                                    System.out.println("here");
                                    for(int i=0; i < terms.length(); i++){
                                        String term = terms.getString(i);
                                        System.out.println("term: " + term);
                                        stringList.add(term);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("skillList: " + stringList);
                                searchListener.classSkillComplete(stringList);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                ArrayList<String> stringList = new ArrayList<>();
                                stringList.add("No match");
                                searchListener.classSkillComplete(stringList);
                            }

                        });

    }

    // interface
    // abstract function declared here
    public interface  OnDoClassSearchComplete {
        public void classSkillComplete(ArrayList<String> message);
    }



}
