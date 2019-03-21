package com.example.socialmediaapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // grab key from previous activity
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
            editGithub.replace(R.id.fragmentContainer, new EditGithubFragment());
            editGithub.commit();
        }
        else if (value == 3){
            FragmentTransaction editLinkedIn = getSupportFragmentManager().beginTransaction();
            editLinkedIn.replace(R.id.fragmentContainer, new EditLinkedInFragment());
            editLinkedIn.commit();
        }
    }

}
