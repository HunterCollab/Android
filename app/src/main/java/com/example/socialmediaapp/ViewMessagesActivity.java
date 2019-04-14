package com.example.socialmediaapp;

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

import com.example.socialmediaapp.loopjtasks.MessagingAPI;
import com.example.socialmediaapp.tools.GeneralTools;

import java.util.ArrayList;

public class ViewMessagesActivity extends AppCompatActivity implements ViewMessagesAdapter.ItemClickListener, MessagingAPI.DownloadComplete {

    private ArrayList< ArrayList<String> > listOfParticipants;
    private RecyclerView recyclerView;
    private ViewMessagesAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ViewMessagesActivity instance;
    private MessagingAPI messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        instance = this;
        messages = new MessagingAPI(getApplicationContext(), instance);
        messages.getListOfMessages();

        // setting up recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.viewMessages_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

    }

    // menu navigation
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
                Intent profIntent = new Intent(this, ProfilePage.class);
                this.startActivity(profIntent);
                return true;
            case R.id.collab_Nav:
                Intent collabIntent = new Intent(this, CollabListActivity.class);
                this.startActivity(collabIntent);
                return true;
            case R.id.nav_messages:
                Intent messageIntent = new Intent(this, ViewMessagesActivity.class);
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

    @Override
    public void onItemClick(View view, int position) {
        //mAdapter.getItem(position)
        /*
        Intent viewMemberProfile = new Intent(getApplicationContext(), ProfilePageOfOthers.class);
        viewMemberProfile.putExtra("memberUsername", membersOfCollab.get(position));
        viewMemberProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(viewMemberProfile);
        */
    }

    @Override
    public void downloadComplete(Boolean success) {
        if (success) {
            // use a linear layout manager
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            // specify an adapter
            mAdapter = new ViewMessagesAdapter(this, messages.getParticipantsAsOneString());
            ((ViewMessagesAdapter) mAdapter).setClickListener(this);
            recyclerView.setAdapter(mAdapter);

            mAdapter.notifyDataSetChanged();
        }
    }
}
