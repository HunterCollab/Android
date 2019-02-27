package com.example.socialmediaapp.loopjtasks;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DoRegister {

    /*
    private final Context context;
    private final OnDoLoginComplete loginCompleteListener;

    public DoLogin(Context context, OnDoLoginComplete listener) {
        this.context = context;
        this.loginCompleteListener = listener;
    }
*/

    //First Step: Create a fucntion that takes email and password as parameters
    public void doRegister(String email, String password){
        //Second Step: Use AsyncHttpClient to execute HTTP requests
        //This creates the client
        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", email);
        requestParams.put("password", password);
        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/user/createUser", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.has("success") && response.getBoolean("success")) { //Success variable is true.
                        System.out.println(response.toString());
                        // TODO: Tell user account was successfully created
                        // TODO: Send user to login page so they can login with the account they just created.
                    } else {
                        String error = response.getString("error"); //Extract the error
                        System.out.println("Error: " + error);
                        // TODO: Push error to screen
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                // TODO: Push error to screen
            }
        });
    }
}
