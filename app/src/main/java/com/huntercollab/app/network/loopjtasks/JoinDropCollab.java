package com.huntercollab.app.network.loopjtasks;

import android.content.Context;
import android.util.Log;

import com.huntercollab.app.config.GlobalConfig;
import com.huntercollab.app.utils.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class JoinDropCollab {

    private Context context;
    private JoinComplete joinListener;
    private LeaveComplete leaveListener;
    private EditComplete editListener;
    private DeleteComplete deleteListener;

    //@author: Hugh Leow
    //@brief:
    //Constructor with multiple listeners for different API calls
    //Listeners pass Boolean to the activity that needs it to check if request was successful
    //@params:
    //[Context context]
    //[JoinComplete joinListener]
    //[LeaveComplete leaveListener]
    //[EditComplete editListener]
    //[DeleteComplete deleteListener]
    public JoinDropCollab(Context context, JoinComplete joinListener, LeaveComplete leaveListener, EditComplete editListener, DeleteComplete deleteListener){
        this.context = context;
        this.joinListener = joinListener;
        this.leaveListener = leaveListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    //@author: Hugh Leow
    //@brief:
    //Used to join a collaboration with the 'collabid'
    //See: CollabDetailFragment.java
    //AsyncHttpClient client
    //ASYNC HTTP POST request, puts 'collabid' into a JSON
    //Sends JSON to the server to request to join the collaboration
    //Returns Boolean 'true' or 'false' to the interface
    //See: CollabDetailFragment.java
    //If request is successful, return Boolean 'true' with a 'message' from the server to the interface
    //if request failed, return Boolean 'false' to the interface
    //@params: [String collabId]
    //@pre condition: No request sent to server to join collaboration
    //@post condition: Request sent to server, receive response for interface
    public void joinCollab(String collabId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/joinCollab";

        JSONObject jsonParams = new JSONObject();

        try {

            jsonParams.accumulate("id",collabId);
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    if (response.has("success")){
                        joinListener.joinComplete(true, "You have joined the collab!");
                    }
                    else {
                        joinListener.joinComplete(false, "Cannot join.");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    String error = responseString;
                    joinListener.joinComplete(false, error);
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //@author: Hugh Leow
    //@brief:
    //Used to leave a collaboration with the 'collabid'
    //See: CollabDetailFragment.java
    //AsyncHttpClient client
    //ASYNC HTTP POST request, puts 'collabid' into a JSON
    //Sends JSON to the server to request to leave the collaboration
    //Returns Boolean 'true' or 'false' to the interface
    //See: CollabDetailFragment.java
    //If request is successful, return Boolean 'true' with a 'message' from the server to the interface
    //if request failed, return Boolean 'false' to the interface
    //@params: [String collabId]
    //@pre condition: No request sent to server to leave collaboration
    //@post condition: Request sent to server, receive response for interface
    public void leaveCollab(String collabId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/leaveCollab";

        JSONObject jsonParams = new JSONObject();

        try {

            jsonParams.accumulate("id",collabId);
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    leaveListener.leaveComplete(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    leaveListener.leaveComplete(false);
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //@author: Hugh Leow
    //@brief:
    //Used to delete a collaboration with the 'collabid' for OWNER of the collaboration
    //See: CollabDetailFragment.java
    //AsyncHttpClient client
    //ASYNC HTTP DELETE request, puts 'collabid' into a JSON
    //Sends JSON to the server to request to delete the collaboration
    //Returns Boolean 'true' or 'false' to the interface
    //See: CollabDetailFragment.java
    //If request is successful, return Boolean 'true' to the interface
    //if request failed, return Boolean 'false' to the interface
    //@params: [String collabId]
    //@pre condition: No request sent to server to delete collaboration
    //@post condition: Request sent to server, receive response for interface
    public void deleteCollab(String collabId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/deleteCollabForReal";

        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.accumulate("id",collabId);
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.delete(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    deleteListener.deleteComplete(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("response", String.valueOf(responseString));
                    deleteListener.deleteComplete(false);
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //@author: Hugh Leow
    //@brief: Interface function to pass Boolean to CollabDetailFragment.java
    //@pre condition: No request sent and/or received
    //@post condition: Response received and Boolean passed
    public interface JoinComplete {

        public void joinComplete(Boolean success, String message);
    }

    //@author: Hugh Leow
    //@brief: Interface function to pass Boolean to CollabDetailFragment.java
    //@pre condition: No request sent and/or received
    //@post condition: Response received and Boolean passed
    public interface LeaveComplete {

        public void leaveComplete(Boolean success);
    }

    //@author: Hugh Leow
    //@brief: Interface function to pass Boolean to CollabDetailFragment.java
    //@pre condition: No request sent and/or received
    //@post condition: Response received and Boolean passed
    public interface EditComplete {

        public void editComplete(Boolean success);
    }

    //@author: Hugh Leow
    //@brief: Interface function to pass Boolean to CollabDetailFragment.java
    //@pre condition: No request sent and/or received
    //@post condition: Response received and Boolean passed
    public interface DeleteComplete {

        public void deleteComplete(Boolean success);
    }
}
