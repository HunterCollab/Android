package com.example.socialmediaapp.loopjtasks;

import android.content.Context;
import android.text.util.Linkify;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MessagingAPI {

    private RequestParams requestParams;
    private Context context;
    private ArrayList< ArrayList<String> > chatMembers;
    private DownloadComplete dataDownloadComplete;

    public MessagingAPI(Context context, DownloadComplete listener){
        this.context = context;
        this.dataDownloadComplete = listener;

        requestParams = new RequestParams();
        chatMembers = new ArrayList<>();
    }

    public void getListOfMessages(){
        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(context);

        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/messaging/myConvos", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                setParticipants(response);
                dataDownloadComplete.downloadComplete(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                dataDownloadComplete.downloadComplete(false);
            }
        });
    }

    public void setParticipants(JSONArray data){
        // parse JSON array (list of ALL chats)
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject jsonobject = data.getJSONObject(i);
                ArrayList<String> membersOfChat = new ArrayList<>();

                // parse JSON array (participants list of each message group)
                JSONArray terms = jsonobject.getJSONArray("participants");
                for(int j = 0; j < terms.length(); j++){
                    String term = terms.getString(j);
                    membersOfChat.add(term);
                }

                // add members of chat array to message list
                chatMembers.add(membersOfChat);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList< ArrayList<String> > getParticipants(){
        return chatMembers;
    }

    public ArrayList<String> getParticipantsAsOneString(){
        ArrayList<String> temp = new ArrayList<>();
        String participants;

        // loop through outer array
        for (int i = 0; i < chatMembers.size(); i++){
            participants = "";
            // loop through inner array
            for (int j = 0; j < chatMembers.get(i).size(); j++){
                participants += chatMembers.get(i).get(j);
                if (j < chatMembers.get(i).size()-1){
                    participants += "\n";
                }
            }
            temp.add(participants);
        }

        return temp;
    }


    public interface DownloadComplete {
        public void downloadComplete(Boolean success);
    }

}