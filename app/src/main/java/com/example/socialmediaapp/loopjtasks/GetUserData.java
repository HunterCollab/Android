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

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class GetUserData {

    RequestParams requestParams;
    private final Context context;
    String jsonResponse;
    ArrayList<String> stringList;

    public GetUserData(Context context){
        this.context = context;
        requestParams = new RequestParams();
        stringList = new ArrayList<>();
    }

    public void getUserData(){
        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);

        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/user/getUserDetails", requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("response: " + response);
                setUserSkills(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void setUserSkills(JSONObject response){

        try {

            System.out.println("Response: " + response);
            JSONArray terms = null;
            terms = response.getJSONArray("skills");

            System.out.println("here");
            for(int i=0; i < terms.length(); i++){
                String term = terms.getString(i);
                System.out.println("term: " + term);
                stringList.add(term);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getUserSkills(){

        System.out.println("stringList" + stringList);
        return stringList;
    }

}
