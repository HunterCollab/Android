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

public class UpdateCollabData {

    //Listener variables
    private Context context;
    private UpdateCollabComplete updateCollabListener;

    //@author: Hugh Leow
    //@brief: Constructor with listeners to pass Boolean 'true' or 'false' for API call to the activity
    //@params: [Context context] [UpdateCollabComplete updateCollabListener]
    public UpdateCollabData(Context context, UpdateCollabComplete updateCollabListener){
        this.context = context;
        this.updateCollabListener = updateCollabListener;
    }

    //@author: Hugh Leow
    //@brief:
    //Used to update the collaboration title
    //Takes the newTitle and collabId parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [String newTitle] [String collabId]
    //@pre condition: Request not sent to server to update collab info
    //@post condition: Request sent to server, receive response for interface
    public void updateCollabTitle(String newTitle, String collabId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("title", newTitle);
            jsonParams.put("id", collabId);

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    updateCollabListener.updateCollabComplete(true);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    updateCollabListener.updateCollabComplete(false);
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //@author: Hugh Leow
    //@brief:
    //Used to update the collaboration size
    //Takes the newSize and collabId parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [String newSize] [String collabId]
    //@pre condition: Request not sent to server to update collab info
    //@post condition: Request sent to server, receive response for interface
    public void updateCollabSize(int newSize, String collabId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("size", newSize);
            jsonParams.put("id", collabId);

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    updateCollabListener.updateCollabComplete(true);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    updateCollabListener.updateCollabComplete(false);
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //@author: Hugh Leow
    //@brief:
    //Used to update the collaboration description
    //Takes the newDescription and collabId parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [String newDescription] [String collabId]
    //@pre condition: Request not sent to server to update collab info
    //@post condition: Request sent to server, receive response for interface
    public void updateCollabDescription(String newDescription, String collabId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("description", newDescription);
            jsonParams.put("id", collabId);

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    updateCollabListener.updateCollabComplete(true);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    updateCollabListener.updateCollabComplete(false);
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //@author: Hugh Leow
    //@brief:
    //Used to update the collaboration location
    //Takes the newLocation and collabId parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [String newLocation] [String collabId]
    //@pre condition: Request not sent to server to update collab info
    //@post condition: Request sent to server, receive response for interface
    public void updateCollabLocation(String newLocation, String collabId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("location", newLocation);
            jsonParams.put("id", collabId);

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    updateCollabListener.updateCollabComplete(true);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    updateCollabListener.updateCollabComplete(false);
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //@author: Hugh Leow
    //@brief:
    //Used to update the collaboration start date and time
    //Takes the newStartDate and collabId parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [long newStartDate] [String collabId]
    //@pre condition: Request not sent to server to update collab info
    //@post condition: Request sent to server, receive response for interface
    public void updateCollabStartDate(long newStartDate, String collabId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("date", newStartDate);
            jsonParams.put("id", collabId);

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    updateCollabListener.updateCollabComplete(true);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    updateCollabListener.updateCollabComplete(false);
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //@author: Hugh Leow
    //@brief:
    //Used to update the collaboration end date and time
    //Takes the newDuration and collabId parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [long newDuration] [String collabId]
    //@pre condition: Request not sent to server to update collab info
    //@post condition: Request sent to server, receive response for interface
    public void updateCollabEndDate(long newDuration, String collabId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("duration", newDuration);
            jsonParams.put("id", collabId);

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    updateCollabListener.updateCollabComplete(true);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    updateCollabListener.updateCollabComplete(false);
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //@author: Hugh Leow
    //@brief:
    //Used to update the collaboration skills requested
    //Takes the skillList and collabId parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [ArrayList<String> skillList] [String collabId]
    //@pre condition: Request not sent to server to update collab info
    //@post condition: Request sent to server, receive response for interface
    public void updateCollabSkills(ArrayList<String> skillList, String collabId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            for (String oneClass : skillList) {
                array.put(oneClass);
            }
            jsonParams.accumulate("skills",array);
            jsonParams.put("id", collabId);

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    updateCollabListener.updateCollabComplete(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    updateCollabListener.updateCollabComplete(false);
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    //@author: Hugh Leow
    //@brief:
    //Used to update the collaboration classes requested
    //Takes the classList and collabId parameter and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [ArrayList<String> classList] [String collabId]
    //@pre condition: Request not sent to server to update collab info
    //@post condition: Request sent to server, receive response for interface
    public void updateCollabClasses(ArrayList<String> classList, String collabId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            for (String oneClass : classList) {
                array.put(oneClass);
            }
            jsonParams.accumulate("classes",array);
            jsonParams.put("id", collabId);

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    updateCollabListener.updateCollabComplete(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    updateCollabListener.updateCollabComplete(false);
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //@author: Hugh Leow
    //@brief:
    //Interface function to pass Boolean:
    //EditCollabDescripFragment.java
    //EditCollabEndFragment.java
    //EditCollabLocationFragment.java
    //EditCollabSizeFragment.java
    //EditCollabStartFragment.java
    //EditCollabTitleFragment.java
    //EditCollabClassesActivity.java
    //EditCollabSkillsActivity.java
    //@pre condition: No request sent and/or response not received
    //@post condition: Response received and values passed
    public interface UpdateCollabComplete {

        public void updateCollabComplete (Boolean success);
    }

}


