package com.huntercollab.app.network.loopjtasks;

import android.content.Context;

import com.huntercollab.app.config.GlobalConfig;
import com.huntercollab.app.utils.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

// FOLLOWED TUTORIAL FROM
// https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui

public class MessagingAPI {

    private RequestParams requestParams;
    private Context context;

    private ArrayList<MessageModel> messages;
    private ArrayList<String> chatIds;
    private ArrayList<String> chatTitles;
    private MessageDownloadComplete dataDownloadComplete;
    private MessageSendComplete messageSent;

    public MessagingAPI(Context context, MessageDownloadComplete listener, MessageSendComplete listener1){
        this.context = context;
        this.dataDownloadComplete = listener;
        this.messageSent = listener1;

        requestParams = new RequestParams();
        chatIds = new ArrayList<>();
        chatTitles = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public void getListOfMessages(){
        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);

        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/messaging/myConvos", requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                setChatList(response);
                dataDownloadComplete.messageDownloadComplete(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                dataDownloadComplete.messageDownloadComplete(false);
            }
        });
    }

    public void retrieveChatroom(int page, String id){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/messaging/getMessages";

        String lastThreeChars = id.substring(id.length() - 3);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("page", page);
            if (lastThreeChars.equals("edu")){
                jsonParams.put("otherUser", id);
            }
            else {
                jsonParams.put("collabId", id);
            }

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    setMessageDetails(response);
                    dataDownloadComplete.messageDownloadComplete(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    dataDownloadComplete.messageDownloadComplete(false);
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message, String id){

        AsyncHttpClient client = GeneralTools.createAsyncHttpClient(context);

        String restApiUrl = GlobalConfig.BASE_API_URL + "/messaging/sendMessage";

        String lastThreeChars = id.substring(id.length() - 3);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("message", message);
            if (lastThreeChars.equals("edu")){
                jsonParams.put("recipient", id);
            }
            else {
                jsonParams.put("collabId", id);
            }

            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(context, restApiUrl, entity,"application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    messageSent.messageSendComplete(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    messageSent.messageSendComplete(false);
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setMessageDetails(JSONArray data){
        // parse JSON array (list of ALL chats)
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject jsonobject = data.getJSONObject(i);
                ArrayList<String> temp = new ArrayList<>();

                // parse JSON array (messages)
                JSONArray message = jsonobject.getJSONArray("messages");
                for(int j = 0; j < message.length(); j++){
                    JSONObject tmp = message.getJSONObject(j);

                    String sender = tmp.getString("sender");
                    String msg = tmp.getString("message");
                    String displayName = tmp.getString("dispName");
                    long time = tmp.getLong("time");

                    // create message and store in array list
                    MessageModel tmpMessage = new MessageModel(sender, msg, time, displayName);
                    messages.add(tmpMessage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setChatList (JSONArray data){
        // parse JSON array (list of ALL chats)
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject jsonobject = data.getJSONObject(i);
                String chatId = new String();
                String chatTitle = new String();
                chatTitle = jsonobject.getString("title");

                if (jsonobject.has("otherUser")) {
                    chatId = jsonobject.getString("otherUser");
                }
                else {
                    JSONObject collabId = jsonobject.getJSONObject("_id");
                    chatId = collabId.getString("$oid");
                }

                // add chatId's and titles to respective arrays
                chatIds.add(chatId);
                chatTitles.add(chatTitle);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<MessageModel> getMessages(){
        return messages;
    }

    public ArrayList<String> getChatIds(){
        return chatIds;
    }

    public ArrayList<String> getChatTitles(){
        return chatTitles;
    }

    public interface MessageDownloadComplete {
        public void messageDownloadComplete(Boolean success);
    }

    public interface MessageSendComplete {
        public void messageSendComplete(Boolean success);
    }
}