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

    private RequestParams requestParams;
    private Context context;
    private ArrayList<String> skillStringList;
    private ArrayList<String> classStringList;
    private String username;
    private String github;
    private String linkedIn;


    public GetUserData(Context context){
        this.context = context;
        requestParams = new RequestParams();
        skillStringList = new ArrayList<>();
        classStringList = new ArrayList<>();
        username = new String();
        github = new String();
        linkedIn = new String();
    }

    public void getUserData(){
        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);

        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/user/", requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                setUserSkills(response);
                setUserClasses(response);
                setUserName(response);
                setUserLinkedIn(response);
                setUserGithub(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void setUserName(JSONObject response){

        try {
            username = response.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUserGithub(JSONObject response){
        try {
            username = response.getString("github");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setUserLinkedIn(JSONObject response){
        try {
            username = response.getString("linkedin");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setUserSkills(JSONObject response){

        try {
            JSONArray terms = null;
            terms = response.getJSONArray("skills");

            for(int i=0; i < terms.length(); i++){
                String term = terms.getString(i);
                skillStringList.add(term);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUserClasses(JSONObject response){

        try {
            JSONArray terms = null;
            terms = response.getJSONArray("classes");

            for(int i=0; i < terms.length(); i++){
                String term = terms.getString(i);
                classStringList.add(term);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUserName(){

        return username;
    }

    public String getUserGithub(){

        return github;
    }

    public String getUserLinkedIn(){

        return linkedIn;
    }

    public ArrayList<String> getUserClasses(){

        return classStringList;
    }

    public ArrayList<String> getUserSkills(){

        System.out.println(skillStringList);

        return skillStringList;
    }

}
