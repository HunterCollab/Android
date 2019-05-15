package com.huntercollab.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.socialmediaapp.R;
import com.huntercollab.app.adapter.ConversationAdapter;
import com.huntercollab.app.network.loopjtasks.MessagingAPI;
import com.huntercollab.app.utils.GeneralTools;

import java.util.ArrayList;

public class ConversationsActivity extends AppCompatActivity implements ConversationAdapter.ItemClickListener, MessagingAPI.MessageDownloadComplete,
        MessagingAPI.MessageSendComplete {

    private RecyclerView recyclerView;
    private ConversationAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ConversationsActivity instance;
    private MessagingAPI messages;

    private ArrayList<String> arrayOfChatIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        instance = this;

        //@author: Hugh Leow
        //@brief:
        //API call to retrieve list of active conversations from the database
        //See: MessagingAPI.java
        messages = new MessagingAPI(getApplicationContext(), instance, instance);
        messages.getListOfMessages();

        //@author: Hugh Leow
        //@brief: Recycler view to display the list of active conversations inside 'messages'
        // setting up recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.viewMessages_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

    }

    //@author: Hugh Leow
    //@brief: Opens the navigation menu for user to use
    //@pre condition: No menu for user to navigate the application
    //@post condition: Menu for user to navigate the application
    //@return: Boolean 'true' or 'false' if selected item is valid
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }

    // menu select
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                Intent profIntent = new Intent(this, ProfileActivity.class);
                this.startActivity(profIntent);
                return true;
            case R.id.collab_Nav:
                Intent collabIntent = new Intent(this, CollabListActivity.class);
                this.startActivity(collabIntent);
                return true;
            case R.id.nav_messages:
                Intent messageIntent = new Intent(this, ConversationsActivity.class);
                this.startActivity(messageIntent);
                this.finish();
                return true;
            case R.id.nav_logout:
                GeneralTools.doRestart(this);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //@author: Hugh Leow
    //@brief:
    //User can view the conversation they click on
    //Moves them to MessagingActivity.java
    //@params: [View view] [int position]
    //@pre condition: User sees list of active conversations, but not the conversation itself
    //@post condition: User is moved to another screen to view all the messages of the conversation they click
    @Override
    public void onItemClick(View view, int position) {
        //mAdapter.getItem(position)
        Intent viewChat = new Intent(getApplicationContext(), MessagingActivity.class);
        viewChat.putExtra("chatId", arrayOfChatIds.get(position));
        viewChat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(viewChat);
    }

    //@author: Hugh Leow
    //@brief:
    //Interface function for ASYNC HTTP request from MessagingAPI.java
    //If list of conversations was retrieved successfully, send information to ConversationAdapter to populate the recycler view for display
    //@params: [Boolean success]
    //@pre condition: List of active conversations have not been retrieved
    //@post condition: List of active conversations retrieved if success = 'true'
    @Override
    public void messageDownloadComplete(Boolean success) {
        if (success) {
            // use a linear layout manager
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            arrayOfChatIds = messages.getChatIds();

            // specify an adapter
            mAdapter = new ConversationAdapter(this, messages.getChatTitles());
            ((ConversationAdapter) mAdapter).setClickListener(this);
            recyclerView.setAdapter(mAdapter);

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void messageSendComplete(Boolean success) {

    }

}
