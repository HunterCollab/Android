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

public class GetCollabsData {

    //Listener variables
    private Context context;
    private GetCollabDataComplete listener;
    private AddCollabComplete addCollabListener;
    private ArrayList<CollabModel> collabs;

    //@author: Hugh Leow & Edwin Quintuna
    //@brief:
    //Constructor with multiple listeners for different API calls
    //Listeners pass Boolean to the activity that needs it to check if request was successful
    //@params: [Context context] [GetCollabDataComplete listener] [AddCollabComplete addCollabListener]
    public GetCollabsData(Context context, GetCollabDataComplete listener, AddCollabComplete addCollabListener){
        this.context = context;
        this.listener = listener;
        this.addCollabListener = addCollabListener;
        collabs = new ArrayList<>();
    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief:
    //Used for the spinner in CollabListActivity.java
    //Takes in a 'collabType' parameter and sends the request to the server
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP GET request, receives a JSON from the server
    //Returns Boolean 'true' or false' to the interface
    //See: CollabListActivity.java
    //If request is successful, build the dataset with public void setCollabDetails(JSONArray collabData)
    //@params: [String collabType]
    //@pre condition: No collaborations retrieved from database
    //@post condition: Collaborations retrieved from database based on parameter
    public void getCollabs(String collabType){

        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);

        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/collab/" + collabType, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("response" , String.valueOf(response));
                setCollabDetails(response);
                listener.getAllCollabs(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.getAllCollabs(false);
            }
        });

    }

    //@author: Hugh Leow
    //@brief:
    //Used for the spinner in CollabListActivity.java for recommended collaborations
    //Takes in the user's 'skills' and 'classes' as arguments and sends request to the server
    //'skills' and 'classes' sent as JSONArrays inside a JSONObject
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends the JSONObject to the server
    //Returns Boolean 'true' or 'false' to the interface
    //See: CollabListActivity.java
    //If request is successful, build the dataset with public void setCollabDetails(JSONArray collabData)
    //@params: [ArrayList<String> skills] [ArrayList<String> classes]
    //@pre condition: No collaborations retrieved from database
    //@post condition: Recommended collaborations retrieved from database
    public void getCollabs(ArrayList<String> skills, ArrayList<String> classes){

        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/getRecommendedCollabs";

        JSONObject jsonParams = new JSONObject();
        try {
            JSONArray skillArray = new JSONArray();
            for (String skill : skills) {
                skillArray.put(skill);
                jsonParams.put("skills", skillArray);
            }
            JSONArray classArray = new JSONArray();
            for (String Class : classes) {
                classArray.put(Class);
                jsonParams.put("classes", classArray);
            }

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            asyncHttpClient.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("response" , String.valueOf(response));
                setCollabDetails(response);
                listener.getAllCollabs(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.getAllCollabs(false);
            }
        });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Takes the data returned from getCollabs() and builds the dataset with an array of CollabModel(s)
    //@params: [JSONArray collabData]
    //@pre condition: Collaboration data not created
    //@post condition: Collaboration created in a data set
    public void setCollabDetails(JSONArray collabData){

        for(int i = 0; i < collabData.length(); i++){
            try {
                JSONObject tmp = collabData.getJSONObject(i);

                JSONObject collabId = (JSONObject) tmp.getJSONObject("_id");
                String id = collabId.getString("$oid");

                String owner = tmp.getString("owner");
                int size = tmp.getInt("size");

                long duration = tmp.getLong("duration");
                String location = tmp.getString("location");
                Boolean status = tmp.getBoolean("status");
                String title = tmp.getString("title");
                String description = tmp.getString("description");

                long dateStr = tmp.getLong("date");

                JSONArray classes = tmp.getJSONArray("classes");
                ArrayList<String> classArray = new ArrayList<String>();
                if (classes != null){
                    int len = classes.length();
                    for (int j = 0; j < len; j++){
                        classArray.add(classes.get(j).toString());
                    }
                }

                JSONArray skills = tmp.getJSONArray("skills");
                ArrayList<String> skillArray = new ArrayList<String>();
                if (skills != null){
                    int len = skills.length();
                    for (int j = 0; j < len; j++){
                        skillArray.add(skills.get(j).toString());
                    }
                }

                JSONArray applicants = tmp.getJSONArray("applicants");
                ArrayList<String> applicantArray = new ArrayList<String>();
                if (applicants != null){
                    int len = applicants.length();
                    for (int j = 0; j < len; j++){
                        applicantArray.add(applicants.get(j).toString());
                    }
                }

                JSONArray members = tmp.getJSONArray("members");
                ArrayList<String> memberArray = new ArrayList<String>();
                if (members != null){
                    int len = members.length();
                    for (int j = 0; j < len; j++){
                        memberArray.add(members.get(j).toString());
                    }
                }

                CollabModel tmpCollab =
                        new CollabModel( i, owner, size, duration, dateStr,
                                location, status, title, description, classArray, skillArray, applicantArray, memberArray, id);

                collabs.add(tmpCollab);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    //@author: Hugh Leow
    //@ brief:
    //Takes all user input from AddCollabActivity.java
    //User input values put into a JSONObject for the HTTP request
    //AsyncHttpClient client
    //ASYNC HTTP POST request, sends the JSONObject to the server
    //Returns Boolean 'true' or 'false' to the interface
    //See: AddCollabActivity.java
    //@params:
    //[String title]
    //[String location]
    //[String description]
    //[Integer size]
    //[ArrayList<String> skills]
    //[ArrayList<String> classes]
    //[long time]
    //[long duration]
    //@pre condition: Collaboration not added to the database
    //@post condition: Request to add collaboration, receive response for interface
    public void addCollab(String title, String location, String description, Integer size, ArrayList<String> skills, ArrayList<String> classes, long time, long duration){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/createCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("title", title);
            jsonParams.put("location", location);
            jsonParams.put("description", description);
            jsonParams.put("size", size);
            jsonParams.put("date", time);
            jsonParams.put("duration", duration);

            JSONArray skillArray = new JSONArray();
            for (String skill : skills) {
                skillArray.put(skill);
                jsonParams.put("skills", skillArray);
            }
            JSONArray classArray = new JSONArray();
            for (String Class : classes) {
                classArray.put(Class);
                jsonParams.put("classes", classArray);
            }

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    addCollabListener.addCollabComplete(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    addCollabListener.addCollabComplete(false);
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Returns 'collabs' dataset created from setCollabDetails(JSONArray collabData)
    //@return: An ArrayList<CollabModel> of all the collaborations retrieved
    public ArrayList<CollabModel> returnCollabs(){
        return collabs;
    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Interface function to pass Boolean to CollabListActivity.java
    //@pre condition: No request sent and/or response not received
    //@post condition: Response received and values passed
    public interface GetCollabDataComplete {

        public void getAllCollabs(Boolean success);
    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Interface function to pass Boolean to AddCollabActivity.java
    //@pre condition: No request sent and/or response not received
    //@post condition: Response received and values passed
    public interface AddCollabComplete {

        public void addCollabComplete (Boolean success);
    }

}


