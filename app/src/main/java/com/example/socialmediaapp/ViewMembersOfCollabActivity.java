package com.example.socialmediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewMembersOfCollabActivity extends AppCompatActivity implements ViewMembersAdapter.ItemClickListener{

    private String currentUser;
    private ArrayList<String> membersOfCollab;
    private ArrayList<String> membersOfCollabNicknames;
    private RecyclerView recyclerView;
    private ViewMembersAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members_of_collab);

        // grab array from previous activity
        Bundle x = getIntent().getExtras();
        if (x != null) {
            membersOfCollab = x.getStringArrayList("membersList");
            membersOfCollabNicknames = x.getStringArrayList("membersListNicknames");
            currentUser = x.getString("currentUser");
        }

        // setting up recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.viewMembers_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new ViewMembersAdapter(this, membersOfCollabNicknames);
        ((ViewMembersAdapter) mAdapter).setClickListener(this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onItemClick(View view, int position) {
        //mAdapter.getItem(position)
        Intent viewMemberProfile = new Intent(getApplicationContext(), ProfilePageOfOthers.class);
        for (int i = 0; i < membersOfCollab.size(); i++){
            System.out.println(membersOfCollab.get(i));
        }
        for (int i = 0; i < membersOfCollabNicknames.size(); i++){
            System.out.println(membersOfCollabNicknames.get(i));
        }
        viewMemberProfile.putExtra("memberUsername", membersOfCollab.get(position));
        viewMemberProfile.putExtra("currentUser", currentUser);
        viewMemberProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(viewMemberProfile);
    }

}
