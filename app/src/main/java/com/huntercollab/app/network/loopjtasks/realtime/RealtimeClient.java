package com.huntercollab.app.network.loopjtasks.realtime;

import android.content.Context;

import com.huntercollab.app.config.GlobalConfig;
import com.loopj.android.http.PersistentCookieStore;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;

public class RealtimeClient {

    private Socket socket;

    public RealtimeClient(Context context) throws RealtimeException, IOException {
        PersistentCookieStore cookieStore = new PersistentCookieStore(context.getApplicationContext());
        List<Cookie> cks = cookieStore.getCookies();
        String token = null;
        for (Cookie c : cks) {
            if (c.getName().equals("capstoneAuth")) {
                token = c.getValue();
            }
        }
        if (token == null) {
            throw new RealtimeException("Auth token not found.");
        }

        socket = new Socket(GlobalConfig.HOST, GlobalConfig.RMS_PORT);


    }
}
