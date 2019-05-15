package com.huntercollab.app.network.loopjtasks;

import android.content.Context;
import android.util.Log;

import com.huntercollab.app.config.GlobalConfig;
import com.huntercollab.app.utils.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class DoRegister {

    private final Context context;
    private final OnDoRegisterComplete registerCompleteListener;

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Constructor with a listener for the register function
    //@params: [Context context] [OnDoRegisterComplete listener]
    public DoRegister(Context context, OnDoRegisterComplete listener) {
        this.context = context;
        this.registerCompleteListener = listener;
    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief:
    //Takes user input for 'email' and 'password' and sends to the server
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP PUT request, sends 'email' and 'password' as a JSON
    //If registration successful, return Boolean 'true' to the interface function
    //See: RegisterActivity.java
    //If successful, stores the authentication token for future use with other API calls on protected routes
    //If unsuccessful, return Boolean 'false' and error message from the server to the interface function
    //@params: [String email] [String password]
    //@pre condition: No request sent to the server to register
    //@post condition: Request sent to server to register, receive response for interface
    public void doRegister(String email, String password) {
        // Second Step: Use AsyncHttpClient to execute HTTP requests
        // This creates the client
        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);

        //https://huntercollabapi.herokuapp.com/user?username=testuser69@myhunter.cuny.edu&password=password
        asyncHttpClient.put(GlobalConfig.BASE_API_URL + "/user?username=" + email + "&password=" + password, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.has("success") && response.getBoolean("success") == true) { //Success variable is true.
                        // extract the token
                        String token = response.getString("token"); //Extract the token
                        // Retrieve cookie store for this application context.
                        PersistentCookieStore myCookieStore = new PersistentCookieStore(context.getApplicationContext());
                        // Create & save cookie into the cookie store.
                        BasicClientCookie newCookie = new BasicClientCookie("capstoneAuth", token);
                        newCookie.setDomain("13.58.204.157");
                        newCookie.setPath("/");
                        myCookieStore.addCookie(newCookie);
                        Log.i ( "token", "Token successfully retrieved and saved to cookie store: " + token);
                        registerCompleteListener.registerCompleted(true, token);
                    } else {
                        String error = response.getString("error"); //Extract the error
                        registerCompleteListener.registerCompleted(false, error);
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        });
    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Interface function to pass Boolean and String to RegisterActivity.java
    //@pre condition: No request sent and/or response not received
    //@post condition: Response received and values passed
    public interface OnDoRegisterComplete {
        public void registerCompleted(Boolean success, String message);
    }

}
