package com.example.socialmediaapp.loopjtasks;

import android.content.Context;
import android.util.Log;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class JoinDropCollab {

    private Context context;
    private JoinDropComplete listener;

    public JoinDropCollab(Context context, JoinDropComplete listener){
        this.context = context;
        this.listener = listener;
    }

    public void joinCollab(String userId){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/collab/joinCollab";

        JSONObject jsonParams = new JSONObject();

        try {

            jsonParams.accumulate("id",userId);
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("response", String.valueOf(response));
                    listener.joinDropComplete(String.valueOf(response));
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

    public interface JoinDropComplete {

        public void joinDropComplete(String success);
    }
}
