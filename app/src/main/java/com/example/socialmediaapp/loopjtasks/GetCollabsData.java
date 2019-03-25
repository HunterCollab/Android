package com.example.socialmediaapp.loopjtasks;

import android.content.Context;
import android.util.Log;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class GetCollabsData {

    // TODO: COLLABS WITH 1 SKILL OR 1 CLASS CANNOT BE DISPLAYED/CRASHES THE APP
    //Listener variables
    private Context context;
    private GetCollabDataComplete listener;
    private ArrayList<CollabModel> collabs;

    public GetCollabsData(Context context, GetCollabDataComplete listener){
        this.context = context;
        this.listener = listener;
        collabs = new ArrayList<>();

    }

    public void getCollabs(String collabType){

        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);

        System.out.println(GlobalConfig.BASE_API_URL + "/collab/" + collabType);
        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/collab/" + collabType, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                //Log.i("response" , String.valueOf(response));
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

                String owner = tmp.getString("owner");
                int size = tmp.getInt("size");
                JSONArray members = tmp.getJSONArray("members");
                //int date = tmp.getInt("date");
                String duration = tmp.getString("duration");
                String location = tmp.getString("location");
                Boolean status = tmp.getBoolean("status");
                String title = tmp.getString("title");
                String description = tmp.getString("description");
                JSONArray classes = tmp.getJSONArray("classes");
                JSONArray skills = tmp.getJSONArray("skills");
                JSONArray applicants = tmp.getJSONArray("applicants");

                CollabModel tmpCollab =
                        new CollabModel( i, owner, size, members, duration,
                                                     location, status, title, description, classes, skills,applicants);

                collabs.add(tmpCollab);



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void addCollab(String title, String location, String description, Integer size, ArrayList<String> skills, ArrayList<String> classes){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/createCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("title", title);
            jsonParams.put("location", location);
            jsonParams.put("description", description);
            jsonParams.put("size", size);
            for (String skill : skills) {
                jsonParams.accumulate("skills", skill);
            }
            for (String Class : classes) {
                jsonParams.accumulate("classes", Class);
            }

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

    }

    public ArrayList<CollabModel> returnCollabs(){
        return collabs;
    }


    public interface GetCollabDataComplete {

        public void getAllCollabs(Boolean success);
    }

}


