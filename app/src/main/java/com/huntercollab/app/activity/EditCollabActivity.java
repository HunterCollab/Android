package com.huntercollab.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.huntercollab.app.fragment.EditCollabDescripFragment;
import com.huntercollab.app.fragment.EditCollabEndFragment;
import com.huntercollab.app.fragment.EditCollabLocationFragment;
import com.huntercollab.app.fragment.EditCollabSizeFragment;
import com.huntercollab.app.fragment.EditCollabStartFragment;
import com.huntercollab.app.fragment.EditCollabTitleFragment;
import com.example.socialmediaapp.R;

public class EditCollabActivity extends AppCompatActivity implements EditCollabTitleFragment.OnDataPass, EditCollabSizeFragment.OnSizePass {

    private int value = 0;
    private int size = 0;
    public String collabId = "";

    //@author: Hugh Leow
    //@brief:
    //Fragments built on this activity based on key values from previous activity/fragment (CollabDetailActivity.java / CollabDetailFragment.java)
    //See: Associated 'fragment' files
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_collab);

        // grab key from previous activity
        Bundle x = getIntent().getExtras();
        if (x != null) {
            value = x.getInt("key");
            size = x.getInt("numOfMembers");
            collabId = x.getString("collabId");
        }

        // edit collab fragments
        if (value == 1) {
            FragmentTransaction editTitle = getSupportFragmentManager().beginTransaction();
            editTitle.replace(R.id.fragmentContainer, new EditCollabTitleFragment());
            editTitle.commit();
        } else if (value == 2) {
            FragmentTransaction editDescription = getSupportFragmentManager().beginTransaction();
            editDescription.replace(R.id.fragmentContainer, new EditCollabDescripFragment());
            editDescription.commit();
        } else if (value == 3) {
            FragmentTransaction editLocation = getSupportFragmentManager().beginTransaction();
            editLocation.replace(R.id.fragmentContainer, new EditCollabLocationFragment());
            editLocation.commit();
        } else if (value == 4) {
            FragmentTransaction editStartDate = getSupportFragmentManager().beginTransaction();
            editStartDate.replace(R.id.fragmentContainer, new EditCollabStartFragment());
            editStartDate.commit();
        } else if (value == 5) {
            FragmentTransaction editEndDate = getSupportFragmentManager().beginTransaction();
            editEndDate.replace(R.id.fragmentContainer, new EditCollabEndFragment());
            editEndDate.commit();
        } else if (value == 9) {
            FragmentTransaction editSize = getSupportFragmentManager().beginTransaction();
            editSize.replace(R.id.fragmentContainer, new EditCollabSizeFragment());
            editSize.commit();
        }

    }

    //@author: Hugh Leow
    //@brief:
    //Interface function
    //Used in the fragment(s) to edit the correct collaboration (using collab id)
    //@return: collaboration id string
    @Override
    public String onDataPass() {
        return collabId;
    }

    //@author: Hugh Leow
    //@brief:
    //Interface function
    //Used in "edit size of fragment" for error checking
    //@return: size of collaboration int
    @Override
    public int onSizePass() {
        return size;
    }
}
