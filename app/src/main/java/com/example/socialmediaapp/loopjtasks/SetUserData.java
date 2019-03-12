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

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SetUserData {

    private Context context = null;
    private OnSkillUpdateComplete skillUpdateComplete;
    private AsyncHttpClient client;

    public SetUserData(Context context, OnSkillUpdateComplete listener){
        this.context = context;
        this.skillUpdateComplete = listener;

    }

    public void setUserSkills(ArrayList<String> skillList){


        System.out.println(skillList);
        client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/user/skills";

        JSONObject jsonParams = new JSONObject();
        try {

            for (String skill : skillList) {
                jsonParams.accumulate("skills",skill);
            }
            System.out.println("skillList" + skillList);
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


        skillUpdateComplete.skillUpdateComplete(true, "SKills Update Completed");

    }

    public interface OnSkillUpdateComplete {

        public void skillUpdateComplete(Boolean success, String message);

    }


}
