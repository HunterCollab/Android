package com.huntercollab.app.utils;
import com.huntercollab.app.activity.LoginActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;

public class GeneralTools {

    //Creates an ASyncHttpClient, sets cookies, and returns it.
    public static AsyncHttpClient createAsyncHttpClient(Context context) {
        AsyncHttpClient client = new AsyncHttpClient();
        //Retrieve cookie store for this application context.
        PersistentCookieStore cookieStore = new PersistentCookieStore(context.getApplicationContext());
        //attach those cookies to the HttpClient which will be making the request
        client.setCookieStore(cookieStore);
        client.setTimeout(30 * 1000); // 60 second timeout
        return client;
    }

    //Creates an ASyncHttpClient, doesn't set cookies.
    public static AsyncHttpClient createAsyncHttpClient() {
        AsyncHttpClient client = new AsyncHttpClient();

        return client;
    }

    // Gets JWT authentication token from the server
    public static String getAuthToken(Context context) {
        PersistentCookieStore cookieStore = new PersistentCookieStore(context.getApplicationContext());
        List<Cookie> cks = cookieStore.getCookies();
        for (Cookie c : cks) {
            if (c.getName().equals("capstoneAuth")) {
                return c.getValue();
            }
        }
        return null;
    }

    // Logs user out and sends them back to the login screen
    // restarts the app
    public static void doRestart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        //Runtime.getRuntime().exit(0);
    }

}
