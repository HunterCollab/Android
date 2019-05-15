package com.huntercollab.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.socialmediaapp.R;
import com.huntercollab.app.fragment.EditGitHubFragment;
import com.huntercollab.app.fragment.EditLinkedInFragment;
import com.huntercollab.app.fragment.EditNameFragment;

public class EditProfileActivity extends AppCompatActivity {

    //@author: Hugh Leow
    //@brief:
    //Fragments built on this activity based on key values from previous activity (ProfleActivity.java)
    //See: Associated 'fragment' files
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // grab key from previous activity
        // and build fragments based on key
        Bundle x = getIntent().getExtras();
        int value = 0;
        if (x != null)
            value = x.getInt("key");

        if (value == 1){
            FragmentTransaction editName = getSupportFragmentManager().beginTransaction();
            editName.replace(R.id.fragmentContainer, new EditNameFragment());
            editName.commit();
        }
        else if (value == 2){
            FragmentTransaction editGithub = getSupportFragmentManager().beginTransaction();
            editGithub.replace(R.id.fragmentContainer, new EditGitHubFragment());
            editGithub.commit();
        }
        else if (value == 3){
            FragmentTransaction editLinkedIn = getSupportFragmentManager().beginTransaction();
            editLinkedIn.replace(R.id.fragmentContainer, new EditLinkedInFragment());
            editLinkedIn.commit();
        }
    }

}
