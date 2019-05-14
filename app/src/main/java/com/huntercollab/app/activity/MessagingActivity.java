package com.huntercollab.app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.huntercollab.app.network.loopjtasks.realtime.RealtimeAsync;
import com.huntercollab.app.utils.Interfaces;

import java.util.ArrayList;
import java.util.Collections;

public class MessagingActivity extends AppCompatActivity implements MessagingAPI.MessageDownloadComplete, MessagingAPI.MessageSendComplete,
        Interfaces.DownloadComplete {
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
    private RealtimeAsync realtimeAync;

    private Handler mHandlerThread;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        instance = this;
        typeMessage = (EditText) findViewById(R.id.edittext_chatbox);
        sendMessage = (Button) findViewById(R.id.button_chatbox_send);

        // used to grab users email and send to adapter
        // for checks when building the recycler view for display
        userDetails = new GetUserData(getApplicationContext(), instance, null, null);
        userDetails.getUserData();

        // Adapter used to display and update messages retrieved from database displayed in recycler view
        // See: MessagesAdapter.java
        mMessageAdapter = new MessagesAdapter(getApplicationContext(), null, null);

        // Used for API call to retrieve messages from the database
        messagingAPI = new MessagingAPI(getApplicationContext(), this, this);

        // setting up recyclerview
        // View used to display the information retrieved from the database, view is built using the mMessageAdapter
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageRecycler.setAdapter(mMessageAdapter);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mMessageRecycler.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        this.layoutManager = mLinearLayoutManager;
        mMessageRecycler.setLayoutManager(layoutManager);

        // grab members from previous activity
        Bundle x = getIntent().getExtras();
        if (x != null)
            chatId = x.getString("chatId");

        // send message button
        // If field is empty, notify user
        // API call to the database using messagingAPI
        // See: MessagingAPI.java
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

        typeMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                mMessageRecycler.scrollToPosition(mMessageAdapter.getItemCount() - 1);
            }
        });

        typeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageRecycler.scrollToPosition(mMessageAdapter.getItemCount() - 1);
            }
        });


    }

    // Interface function for ASYNC HTTP requests from MessagingAPI.java
    // Retrieves messages from the database and passes the data through mMessageAdapter to build the display
    // See: MessagingAPI.java
    @Override
    public void messageDownloadComplete(Boolean success) {
        if (success) {
            messages = messagingAPI.getMessages();
            Collections.reverse(messages);

            mMessageAdapter.setMessages(messages);
            mMessageAdapter.notifyDataSetChanged();
            mMessageRecycler.scrollToPosition(mMessageAdapter.getItemCount() - 1);
        }
    }

    // Interface function for ASYNC HTTP request from MessagingAPI.java
    // If sending a message is successful, we add the message to the user's screen locally
    // See: MessagingAPI.java
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

    // Interface function for ASYNC HTTP request from GetUserData.java
    // When userDetails is successful in retrieving the data, the user's email is set in a string, and the API is called to retrieve the chat messages
    @Override
    public void downloadComplete(Boolean success) {
        user = userDetails.getUserName();
        mMessageAdapter.setUser(user);
        messagingAPI.retrieveChatroom(0, chatId);
    }

    // Refreshes the chatroom when user resumes the application
    public void refreshChatroom() {
        messagingAPI.retrieveChatroom(0, chatId);
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onResume() {
        super.onResume();
        mHandlerThread = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                refreshChatroom();
            }
        };
        this.startRealtimeConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("OnStop Called. Clearing RMS Connection.");
        this.killRealtimeConnection();
    }

    // Starts the real time connection to the server
    public void startRealtimeConnection() {
        this.killRealtimeConnection();
        this.realtimeAync = new RealtimeAsync();
        //Start realtime connection
        this.realtimeAync.execute(this);
    }

    // Stops the real time connection to the server
    public void killRealtimeConnection() {
        if (this.realtimeAync != null) {
            this.realtimeAync.killConn();
            this.realtimeAync.cancel(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public Handler getHandlerThread() {
        return mHandlerThread;
    }
}
