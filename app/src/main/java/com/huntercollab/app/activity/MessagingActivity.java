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

    //@author: Hugh Leow
    //@brief: Array that holds the message objects retrieved from the database
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

        //@author: Hugh Leow
        //@brief:
        //Used for API call for current user data
        //for checks when building the recycler view for display
        //See: GetUserData.java
        userDetails = new GetUserData(getApplicationContext(), instance, null, null);
        userDetails.getUserData();

        //@author: Hugh Leow
        //@brief:
        //Adapter used to display and update messages retrieved from database displayed in recycler view
        //See: MessagesAdapter.java
        mMessageAdapter = new MessagesAdapter(getApplicationContext(), null, null);

        //@author: Hugh Leow
        //@brief:
        //Used for API call to retrieve messages from the database
        //See: MessagingAPI.java
        messagingAPI = new MessagingAPI(getApplicationContext(), this, this);

        //@author: Hugh Leow
        //@brief:
        //View used to display the information retrieved from the database, view is built using the mMessageAdapter
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

        //@author: Hugh Leow
        //@brief:
        //Used to send message to database
        //If field is empty, notify user
        //API call to the database using messagingAPI
        //See: MessagingAPI.java
        //@pre condition: Message is in text box, no request sent
        //@post condition: Request sent to send message, text box cleared
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

    //@author: Hugh Leow
    //@brief:
    //Interface function for ASYNC HTTP request from MessagingAPI.java
    //If retrieving messages from the database is successful, passes the data through mMessageAdapter to build the display
    //See: MessagingAPI.java
    //@params: [Boolean success]
    //@pre condition: Request for messages not retrieved
    //@post condition: Messages retrieved successfully for chatroom if success = 'true'
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

    //@author: Hugh Leow
    //@brief:
    //Interface function for ASYNC HTTP request from MessagingAPI.java
    //If sending a message is successful, we add the message to the user's screen locally
    //See: MessagingAPI.java
    //@params: [Boolean success]
    //@pre condition: User message not sent to database
    //@post condition: Message sent to database/chatroom if success = true'
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

    //@author: Hugh Leow
    //@brief:
    //Interface function for ASYNC HTTP request from GetUserData.java
    //When userDetails is successful in retrieving the data, the user's email is set in a string, and the API is called to retrieve the chat messages
    //@params: [Boolean success]
    //@pre condition: User data not retrieved
    //@post condition: User data retrieved if success = 'true'
    @Override
    public void downloadComplete(Boolean success) {
        user = userDetails.getUserName();
        mMessageAdapter.setUser(user);
        messagingAPI.retrieveChatroom(0, chatId);
    }

    //@author: Hugh Leow
    //@brief: Refreshes the chatroom when user resumes the application
    //@pre condition: Chatroom not up to date
    //@post condition: Chatroom is up to date
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

    //@author: Hugh Leow & Ram Vakada
    //@brief: Starts the real time connection to the server
    //@pre condition: Connection to real time server not established
    //@post condition: Connection to real time server established
    public void startRealtimeConnection() {
        this.killRealtimeConnection();
        this.realtimeAync = new RealtimeAsync();
        //Start realtime connection
        this.realtimeAync.execute(this);
    }

    //@author: Hugh Leow & Ram Vakada
    //@brief: Stops the real time connection to the server
    //@pre condition: Connection to the real time server is open
    //@post condition: Connection to the real time server is closed
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
