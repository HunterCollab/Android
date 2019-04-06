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

public class UpdateCollabData {

    //Listener variables
    private Context context;
    private UpdateCollabComplete updateCollabListener;

    public UpdateCollabData(Context context, UpdateCollabComplete updateCollabListener){
        this.context = context;
        this.updateCollabListener = updateCollabListener;
    }

    public void updateCollabTitle(String newTitle){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("title", newTitle);

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

    public void updateCollabDescription(String newDescription){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("description", newDescription);

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

    public void updateCollabLocation(String newLocation){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("description", newLocation);

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

    public void updateCollabStartDate(String newStartDate){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("duration", newStartDate);

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

    public void updateCollabEndDate(String newDuration){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/editCollab";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("duration", newDuration);

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

    public interface UpdateCollabComplete {

        public void updateCollabComplete (Boolean success);
    }

}


