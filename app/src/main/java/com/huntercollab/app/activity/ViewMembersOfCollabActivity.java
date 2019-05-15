package com.huntercollab.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.socialmediaapp.R;
import com.huntercollab.app.adapter.ViewMembersAdapter;

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

        //@author: Hugh Leow
        //@brief: View to display the members of the collaboration
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

    //@author: Hugh Leow
    //@brief:
    //Interface function from ViewMembersAdapter.java
    //Sends user to OtherProfileActivity.java with name of user they want to view
    //@params: [View view] [int position]
    //@pre condition: User sees list of members in collaboration
    //@post condition: User is moved to screen to view the profile of a specific user
    @Override
    public void onItemClick(View view, int position) {
        //mAdapter.getItem(position)
        Intent viewMemberProfile = new Intent(getApplicationContext(), OtherProfileActivity.class);
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
