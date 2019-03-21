package com.example.socialmediaapp.loopjtasks;

import android.content.Context;

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

public class DoRegister {

    private final Context context;
    private final OnDoRegisterComplete registerCompleteListener;

    // constructor
    public DoRegister(Context context, OnDoRegisterComplete listener) {
        this.context = context;
        this.registerCompleteListener = listener;
    }

    // Async task for HTTP request
    // First Step: Create a fucntion that takes email and password as parameters
    public void doRegister(String email, String password) {
        // Second Step: Use AsyncHttpClient to execute HTTP requests
        // This creates the client
        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", email);
        requestParams.put("password", password);
        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/user/createUser", requestParams, new JsonHttpResponseHandler() {

            // pulled from login, testing now
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
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
                        System.out.println("Token successfully retrieved and saved to cookie store: " + token);
                        registerCompleteListener.registerCompleted(true, token);
                    } else {
                        String error = response.getString("error"); //Extract the error
                        System.out.println("Error: " + error);
                        registerCompleteListener.registerCompleted(false, error);
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        });
    }

    // interface
    // abstract function declared here
    public interface OnDoRegisterComplete {
        public void registerCompleted(Boolean success, String message);
    }

}
