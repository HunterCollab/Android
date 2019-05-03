package com.huntercollab.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.huntercollab.app.adapter.MessagesAdapter;
import com.huntercollab.app.network.loopjtasks.GetUserData;
import com.huntercollab.app.network.loopjtasks.MessageModel;
import com.huntercollab.app.network.loopjtasks.MessagingAPI;
import com.huntercollab.app.utils.Interfaces;

import java.util.ArrayList;
import java.util.Collections;

public class MessagingActivity extends AppCompatActivity implements MessagingAPI.MessageDownloadComplete, MessagingAPI.MessageSendComplete,
        Interfaces.DownloadComplete, Interfaces.DownloadProfleComplete, Interfaces.OwnerDownloadComplete {
    private RecyclerView mMessageRecycler;
    private MessagesAdapter mMessageAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String user = "";
    private String chatId;
    private ArrayList<MessageModel> messages = new ArrayList<>();

    private String messageToSend;
    private EditText typeMessage;
    private Button sendMessage;

    private MessagingActivity instance;
    private GetUserData userDetails;

    private MessagingAPI messagingAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        instance = this;
        typeMessage = (EditText) findViewById(R.id.edittext_chatbox);
        sendMessage = (Button) findViewById(R.id.button_chatbox_send);

        // used to grab users email and send to adapter
        userDetails = new GetUserData(getApplicationContext(), instance, instance, instance);
        userDetails.getUserData();

        mMessageAdapter = new MessagesAdapter(getApplicationContext(), null, null);
        messagingAPI = new MessagingAPI(getApplicationContext(), this, this);

        // setting up recyclerview
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mMessageRecycler.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mMessageRecycler.setLayoutManager(layoutManager);

        // grab members from previous activity
        Bundle x = getIntent().getExtras();
        if (x != null)
            chatId = x.getString("chatId");

        // send message button
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage.setEnabled(false);
                messageToSend = typeMessage.getText().toString();
                if (messageToSend.isEmpty()){
                    Toast t = Toast.makeText(getApplicationContext(), "Nothing to send.", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                    sendMessage.setEnabled(true);
                }
                else {
                    messagingAPI.sendMessage(messageToSend, chatId);
                }

            }
        });
    }

    @Override
    public void messageDownloadComplete(Boolean success) {
        if (success) {
            messages = messagingAPI.getMessages();
            Collections.reverse(messages);

            mMessageAdapter = new MessagesAdapter(this, messages, user);
            mMessageRecycler.setAdapter(mMessageAdapter);
            mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void messageSendComplete(Boolean success) {
        if (success) {
            typeMessage.getText().clear();
            MessageModel tmp = new MessageModel(user, messageToSend, System.currentTimeMillis(), "");
            messages.add(tmp);
            mMessageAdapter.notifyDataSetChanged();
            sendMessage.setEnabled(true);
        }
    }

    // abstract function from GetUserData.java defined here
    // populate profile screen with data on successful API call
    @Override
    public void downloadComplete(Boolean success) {
        user = userDetails.getUserName();
        messagingAPI.retrieveChatroom(0, chatId);
    }

    @Override
    public void downloadProfileComplete(Boolean success) {
    }

    @Override
    public void ownerDownloadComplete(Boolean success) {
    }
}
