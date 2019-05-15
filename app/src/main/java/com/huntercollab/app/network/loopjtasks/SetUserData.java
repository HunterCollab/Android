package com.huntercollab.app.network.loopjtasks;

import android.content.Context;
import android.util.Log;

import com.huntercollab.app.config.GlobalConfig;
import com.huntercollab.app.utils.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SetUserData {

    private Context context = null;
    private UpdateComplete updateComplete;
    private AsyncHttpClient client;

    //@author: Hugh Leow
    //@brief: Constructor with a listener to pass Boolean 'true' or 'false' for API call to the activity
    //@params: [Context context] [UpdateComplete listener]
    public SetUserData(Context context, UpdateComplete listener){
        this.context = context;
        this.updateComplete = listener;
    }

    //@author: Hugh Leow
    //@brief:
    //Used to update the user's 'preferred name'
    //Takes the newName parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [String newName]
    //@pre condition: Request not sent to server to update user info
    //@post condition: Request sent to server, receive response for interface
    public void setUserNickname(String newName){

        client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/user";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("name", newName);
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        updateComplete.dataUpdateComplete(true, "Name Updated");

    }

    //@author: Hugh Leow
    //@brief:
    //Used to update the user's Github link
    //Takes the newGithub parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [String newGithub]
    //@pre condition: Request not sent to server to update user info
    //@post condition: Request sent to server, receive response for interface
    public void setUserGithub(String newGithub){

        client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/user";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("github", newGithub);
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        updateComplete.dataUpdateComplete(true, "Github Updated");

    }

    //@author: Hugh Leow
    //@brief:
    //Used to update the user's LinkedIn link
    //Takes the newLinkedIn parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [String newLinkedIn]
    //@pre condition: Request not sent to server to update user info
    //@post condition: Request sent to server, receive response for interface
    public void setUserLinkedIn(String newLinkedIn){

        client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/user";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("linkedin", newLinkedIn);
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        updateComplete.dataUpdateComplete(true, "LinkedIn Updated");

    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief:
    //Used to update the user's skills
    //Takes the skillList parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [ArrayList<String> skillList]
    //@pre condition: Request not sent to server to update user info
    //@post condition: Request sent to server, receive response for interface
    public void setUserSkills(ArrayList<String> skillList){

        client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/user/skills";

        JSONObject jsonParams = new JSONObject();

        try {

                JSONArray array = new JSONArray();
                for (String skill : skillList) {
                    array.put(skill);
                }
                jsonParams.accumulate("skills",array);

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        updateComplete.dataUpdateComplete(true, "SKills Update Completed");

    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief:
    //Used to update the user's classes
    //Takes the classList parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [ArrayList<String> classList]
    //@pre condition: Request not sent to server to update user info
    //@post condition: Request sent to server, receive response for interface
    public void setUserClasses(ArrayList<String> classList){
        client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/user/classes";

        JSONObject jsonParams = new JSONObject();
        try {

            JSONArray array = new JSONArray();
            for (String oneClass : classList) {
                array.put(oneClass);
            }
            jsonParams.accumulate("classes",array);

            System.out.println("classList" + classList);
            System.out.println("jsonParams: " + jsonParams);
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        updateComplete.dataUpdateComplete(true, "Classes Update Completed");

    }

    //@author: Hugh Leow
    //@brief:
    //Interface function to pass the Boolean to:
    //EditNameFragment.java
    //EditGithubFragment.java
    //EditLinkedInFragment.java
    //UserClassesActivity.java
    //UserSkillsActivity.java
    //@pre condition: No request sent and/or not received
    //@post condition: Response received and values passed
    public interface UpdateComplete {

        public void dataUpdateComplete(Boolean success, String message);

    }


}
