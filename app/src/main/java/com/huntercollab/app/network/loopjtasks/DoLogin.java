package com.huntercollab.app.network.loopjtasks;

import android.content.Context;
import android.util.Log;

import com.huntercollab.app.config.GlobalConfig;
import com.huntercollab.app.utils.GeneralTools;
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

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Constructor with a listener for the login function
    //@params: [Context context] [OnDoLoginComplete listener]
    public DoLogin(Context context, OnDoLoginComplete listener) {
        this.context = context;
        this.loginCompleteListener = listener;
    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief:
    //Takes user input, for 'username' and 'password' and sends to the server
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP GET request, sends 'username' and 'password' in a JSON
    //If successful, return Boolean 'true' to the interface function
    //See: LoginActivity.java
    //If successful, stores the authentication token for future use with other API calls on protected routes
    //If unsuccessful, return Boolean 'false' and error message from the server to the interface function
    //@params: [String username] [String password]
    //@pre condition: No request sent to the server to login
    //@post condition: Request sent to server to login, receive response for interface
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
                        myCookieStore.clear();
                        // Create & save cookie into the cookie store.
                        BasicClientCookie newCookie = new BasicClientCookie("capstoneAuth", token);
                        newCookie.setDomain(GlobalConfig.HOST);
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

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Interface function to pass Boolean and String to LoginActivity.java
    //@pre condition: No request sent and/or response not received
    //@post condition: Response received and values passed
    public interface OnDoLoginComplete {

        public void loginCompleted(Boolean success, String message);

    }
}
