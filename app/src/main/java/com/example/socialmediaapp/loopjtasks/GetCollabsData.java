package com.example.socialmediaapp.loopjtasks;

import android.content.Context;
import android.util.Log;

import com.example.socialmediaapp.AddCollabActivity;
import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

    public GetCollabsData(Context context, GetCollabDataComplete listener, AddCollabComplete addCollabListener){
        this.context = context;
        this.listener = listener;
        this.addCollabListener = addCollabListener;
        collabs = new ArrayList<>();
    }

    public void getCollabs(String collabType){

        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);

        System.out.println(GlobalConfig.BASE_API_URL + "/collab/" + collabType);
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

    public void setCollabDetails(JSONArray collabData){

        for(int i = 0; i < collabData.length(); i++){
            try {
                JSONObject tmp = collabData.getJSONObject(i);

                JSONObject collabId = (JSONObject) tmp.getJSONObject("_id");
                String id = collabId.getString("$oid");

                System.out.println("id: " + id);

                String owner = tmp.getString("owner");
                int size = tmp.getInt("size");

                String duration = tmp.getString("duration");
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

    public ArrayList<CollabModel> returnCollabs(){
        return collabs;
    }

    public interface GetCollabDataComplete {

        public void getAllCollabs(Boolean success);
    }

    public interface AddCollabComplete {

        public void addCollabComplete (Boolean success);
    }

}


