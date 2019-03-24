package com.example.socialmediaapp;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.loopjtasks.GetUserData;
import com.example.socialmediaapp.tools.GeneralTools;

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity implements GetUserData.DownloadComplete {

    // TODO: SKILLS + CLASSES OF 0 or 1 CANNOT BE ADDED
    private Context context = ProfilePage.this;
    private TextView userNickname;
    private TextView githubLink;
    private TextView linkedinLink;
    private TextView skills;
    private TextView classes;

    private ArrayList<String> skillsArray;
    private ArrayList<String> classesArray;

    private ProfilePage instance = null;
    private Button editName;
    private Button editGit;
    private Button editLi;
    private Button editSkill;
    private Button editClass;
    private long mLastClickTime = 0;
    private GetUserData userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        instance = this;
        userNickname = (TextView) findViewById(R.id.userName);
        githubLink = (TextView) findViewById(R.id.githubLink);
        linkedinLink = (TextView) findViewById(R.id.linkedinLink);
        skills = (TextView) findViewById(R.id.skillsList);
        classes = (TextView) findViewById(R.id.classesList);
        editName = (Button) findViewById(R.id.editName_button);
        editGit = (Button) findViewById(R.id.editGithub_button);
        editLi = (Button) findViewById(R.id.editLI_button);
        editSkill = (Button) findViewById(R.id.editSkills_button);
        editClass = (Button) findViewById(R.id.editClasses_button);

        userDetails = new GetUserData(getApplicationContext(), instance);
        userDetails.getUserData();

        // editName Button
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prevents user from clicking button multiple times in less than 3 seconds
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000)
                    return;
                mLastClickTime = SystemClock.elapsedRealtime();
                sendUserToEditName();
            }
        });

        // editGit Button
        editGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prevents user from clicking button multiple times in less than 3 seconds
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000)
                    return;
                mLastClickTime = SystemClock.elapsedRealtime();
                sendUserToEditGithub();
            }
        });

        // editLi Button
        editLi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prevents user from clicking button multiple times in less than 3 seconds
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000)
                    return;
                mLastClickTime = SystemClock.elapsedRealtime();
                sendUserToEditLinkedIn();
            }
        });

        // editSkill Button
        editSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prevents user from clicking button multiple times in less than 3 seconds
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000)
                    return;
                mLastClickTime = SystemClock.elapsedRealtime();
                sendUserToSkills();
            }
        });

        // editClass Button
        editClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prevents user from clicking button multiple times in less than 3 seconds
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000)
                    return;
                mLastClickTime = SystemClock.elapsedRealtime();
                sendUserToClasses();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        // API call again to refresh the page with updated data
        userDetails = new GetUserData(getApplicationContext(), instance);
        userDetails.getUserData();
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
                return true;
            case R.id.nav_logout:
                GeneralTools.doRestart(this, LoginActivity.class);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // send User to skills screen so they can edit
    private void sendUserToSkills() {
        Intent skillPage = new Intent (ProfilePage.this, UserSkillsActivity.class);
        startActivity(skillPage);
    }

    // send User to classes screen so they can edit
    private void sendUserToClasses() {
        Intent classPage = new Intent (ProfilePage.this, UserClassesActivity.class);
        startActivity(classPage);
    }

    // send User to editName fragment
    // bundle + key to pass parameter to EditProfileActivity.java
    private void sendUserToEditName() {
        Intent editProfile = new Intent (ProfilePage.this, EditProfileActivity.class);
        Bundle x = new Bundle();
        x.putInt("key",1);
        editProfile.putExtras(x);
        startActivity(editProfile);
    }

    // send User to editGithub fragment
    private void sendUserToEditGithub() {
        Intent editProfile = new Intent (ProfilePage.this, EditProfileActivity.class);
        Bundle x = new Bundle();
        x.putInt("key",2);
        editProfile.putExtras(x);
        startActivity(editProfile);
    }

    // send User to editLinkedIn fragment
    private void sendUserToEditLinkedIn() {
        Intent editProfile = new Intent (ProfilePage.this, EditProfileActivity.class);
        Bundle x = new Bundle();
        x.putInt("key",3);
        editProfile.putExtras(x);
        startActivity(editProfile);
    }

    // abstract function from GetUserData.java defined here
    // populate profile screen with data on successful API cal
    @Override
    public void downloadComplete(Boolean success) {
        if (success) {
            // set text fields to user details
            userNickname.setText(userDetails.getUserNickname());

            // linkify = links lead to browser
            githubLink.setText(userDetails.getUserGithub());
            Linkify.addLinks(githubLink, Linkify.WEB_URLS);

            linkedinLink.setText(userDetails.getUserLinkedIn());
            Linkify.addLinks(linkedinLink, Linkify.WEB_URLS);

            // clear skills + classes, and populate them
            skills.setText(null);
            classes.setText(null);
            skillsArray = userDetails.getUserSkills();
            classesArray = userDetails.getUserClasses();

            for(int i=0; i < skillsArray.size(); i++) {
                skills.append(skillsArray.get(i) + "\n");
            }

            for(int i=0; i < classesArray.size(); i++) {
                classes.append(classesArray.get(i) + "\n");
            }

        } else {
            // show error message to user
            Toast t = Toast.makeText(context, "ERROR", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
        }
    }

}
