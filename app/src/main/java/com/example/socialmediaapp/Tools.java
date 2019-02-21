package com.example.socialmediaapp;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class Tools {

    public static final String BASE_API_URL = "https://huntercollabapi.herokuapp.com";

    public static void printCurrentUserDetails(final Context context) {
        createAsyncHTTPClient(context).get(Tools.BASE_API_URL + "/user/getUserDetails", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println(response.toString());
            }
        });
    }

    //Creates an ASyncHttpClient, sets the cookies, and returns it.
    public static AsyncHttpClient createAsyncHTTPClient(Context context) {
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(context.getApplicationContext());
        client.setCookieStore(cookieStore);
        client.setTimeout(60 * 1000);
        return client;
    }

}
