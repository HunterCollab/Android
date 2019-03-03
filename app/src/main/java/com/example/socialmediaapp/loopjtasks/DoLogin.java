package com.example.socialmediaapp.loopjtasks;

import android.content.Context;
import android.util.Log;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class DoLogin {

    private final Context context;
    private final OnDoLoginComplete loginCompleteListener;

    // constructor
    public DoLogin(Context context, OnDoLoginComplete listener) {
        this.context = context;
        this.loginCompleteListener = listener;
    }

    // Async task for HTTP request
    public void doLogin(String username, String password){
        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", username);
        requestParams.put("password", password);

        // Async variable that calls the createAsyncHttpClient() class
        // "https://huntercollabapi.herokuapp.com" + /auth/login + ?username=admin ?password=admin
        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/auth/login", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i ("response", response.toString());
                try {
                    if (response.has("success") && response.getBoolean("success") == true) { //Success variable is true.
                        String token = response.getString("token"); //Extract the token
                        // Retrieve cookie store for this application context.
                        PersistentCookieStore myCookieStore = new PersistentCookieStore(context.getApplicationContext());
                        // Create & save cookie into the cookie store.
                        BasicClientCookie newCookie = new BasicClientCookie("capstoneAuth", token);
                        newCookie.setDomain("huntercollabapi.herokuapp.com");
                        newCookie.setPath("/");
                        myCookieStore.addCookie(newCookie);
                        Log.i ("token", "Token successfully retrieved and saved to cookie store: " + token);
                        loginCompleteListener.loginCompleted(true, token);
                    } else {
                        String error = response.getString("error"); //Extract the error
                        Log.i ("error", "Error: " + error);
                        loginCompleteListener.loginCompleted(false, error);
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        });
    }

    // interface
    public interface OnDoLoginComplete {
        public void loginCompleted(Boolean success, String message);
    }
}
