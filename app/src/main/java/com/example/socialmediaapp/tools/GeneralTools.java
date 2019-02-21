package com.example.socialmediaapp.tools;

import android.content.Context;

import com.example.socialmediaapp.config.GlobalConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class GeneralTools {

    //Creates an ASyncHttpClient, sets cookies, and returns it.
    public static AsyncHttpClient createAsyncHttpClient(Context context) {
        AsyncHttpClient client = new AsyncHttpClient();
        //Retrieve cookie store for this application context.
        PersistentCookieStore cookieStore = new PersistentCookieStore(context.getApplicationContext());
        //attach those cookies to the HttpClient which will be making the request
        client.setCookieStore(cookieStore);
        client.setTimeout(60 * 1000); // 60 second timeout
        return client;
    }

    //Test function to hit the /user/getUserDetails endpoint and print the response.
    public static void printCurrentUserDetails(final Context context) {
        createAsyncHttpClient(context).get(GlobalConfig.BASE_API_URL + "/user/getUserDetails", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                System.out.println(statusCode + " ::: " + res);
            }
        });
    }

}
