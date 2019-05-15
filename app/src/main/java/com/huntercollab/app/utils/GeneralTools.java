package com.huntercollab.app.utils;
import com.huntercollab.app.activity.LoginActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;

public class GeneralTools {

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Creates an ASYNC HTTP client, sets cookies and returns it
    //@params: [Context context]
    //@pre condition: No authentication in cookies set, or cookies are invalid
    //@post condition: New authentication token set in cookies
    //@return: AsyncHttpClient returned
    public static AsyncHttpClient createAsyncHttpClient(Context context) {
        AsyncHttpClient client = new AsyncHttpClient();
        //Retrieve cookie store for this application context.
        PersistentCookieStore cookieStore = new PersistentCookieStore(context.getApplicationContext());
        //attach those cookies to the HttpClient which will be making the request
        client.setCookieStore(cookieStore);
        client.setTimeout(30 * 1000); // 60 second timeout
        return client;
    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Creates an ASYNC HTTP client, doesn't set cookies
    //@pre condition: cookies already set prior
    //@post condition: access protected routes with cookies
    //@return: AsyncHttpClient returned
    public static AsyncHttpClient createAsyncHttpClient() {
        AsyncHttpClient client = new AsyncHttpClient();

        return client;
    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Gets JWT authentication token from the server
    //@pre condition: No valid JWT token locally stored
    //@post condition: Valid JWT token retrieved and stored
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

    //@author: Hugh Leow
    //@brief: Logs user out and sends them back to the login screen
    //@pre condition: User is logged in
    //@post condition: User is logged out
    public static void doRestart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        //Runtime.getRuntime().exit(0);
    }

}
