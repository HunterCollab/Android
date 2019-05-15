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

    //@author: Hugh Leow
    //@brief:
    //Constructor with multiple listeners for different API calls
    //Listeners pass Boolean to the activity that needs it to check if request was successful
    //@params:
    //[Context context]
    //[MessageDownloadComplete listener]
    //[MessageSendComplete listener1]
    public MessagingAPI(Context context, MessageDownloadComplete listener, MessageSendComplete listener1){
        this.context = context;
        this.dataDownloadComplete = listener;
        this.messageSent = listener1;

        requestParams = new RequestParams();
        chatIds = new ArrayList<>();
        chatTitles = new ArrayList<>();
        messages = new ArrayList<>();
    }

    //@author: Hugh Leow
    //@brief:
    //Used to retrieve a list of active conversations for the user
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP GET request, receives JSON from the server
    //Returns Boolean 'true' to the interface if successful and sets 'chat id' and 'chat title' to their respective arrays using setChatList(JSONArray data)
    //@pre condition: List of active conversations not up to date
    //@post condition: Request for up to date list of conversations sent
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

    //@author: Hugh Leow
    //@brief:
    //Used to retrieve messages from a specific chat (collaboration, or user to user)
    //Takes the chat id and page we want to view as arguments
    //Puts the arguments into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface and call setMessageDetails(JSONArray data) to create the dataset of messages for the chatroom
    //Failure to retrieve returns Boolean 'false' to the interface
    //@params: [int page] [String id]
    //@pre condition: Request for chatroom not sent to server
    //@post condition: Request for up to date chatroom sent to server
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

    //@author: Hugh Leow
    //@brief:
    //Used to send messages to the database using the chat id
    //Takes the chat id and message and puts it into a JSON
    //AsyncHttpClient asyncHttpClient
    //ASYNC HTTP POST request, sends JSON to the server for request
    //If successful, return Boolean 'true' to the interface function
    //If unsuccessful, return Boolean 'false' to the interface function
    //@params: [String message] [String id]
    //@pre condition: No request sent to deliver user's message
    //@post condition: Request sent to server to deliver user's message
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

    //@author: Hugh Leow
    //@brief:
    //Creates the dataset of messages for a particular chatroom (collaboration or user to user) using data returned to us from the API call
    //@params: [JSONArray data]
    //@pre condition: No dataset created for the messages received
    //@post condition: Messages created in a dataset
    public void setMessageDetails(JSONArray data){
        // parse JSON array (list of ALL chats)
        messages = new ArrayList<>();
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

    //@author: Hugh Leow
    //@brief: Takes server response with JSON and parses it to two respective arrays, chat ids and chat titles for a list of active conversations for the user
    //@params: [JSONArray data]
    //@pre condition: List of active conversations not created
    //@post condition: List of active conversations created in a dataset
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

    //@author: Hugh Leow
    //@brief: Interface function to pass Boolean to MessagingActivity.java
    //@pre condition: No request sent and/or response not received
    //@post condition: Response received and values passed
    public interface MessageDownloadComplete {
        public void messageDownloadComplete(Boolean success);
    }

    //@author: Hugh Leow
    //@brief: Interface function to pass Boolean to MessagingActivity.java
    //@pre condition: No request sent and/or response not received
    //@post condition: Response received and values passed
    public interface MessageSendComplete {
        public void messageSendComplete(Boolean success);
    }
}