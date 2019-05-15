package com.huntercollab.app.activity;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.huntercollab.app.network.loopjtasks.GetUserData;
import com.huntercollab.app.utils.GeneralTools;
import com.huntercollab.app.utils.Interfaces;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements Interfaces.DownloadComplete, Interfaces.DownloadProfleComplete,
        Interfaces.OwnerDownloadComplete {

    private Context context = ProfileActivity.this;
    private TextView userNickname;
    private TextView githubLink;
    private TextView linkedinLink;
    private TextView skills;
    private TextView classes;
    private ImageView profilePic;

    private String profilePicLink = "https://i.imgur.com/EK6jPjm.png";
    private ArrayList<String> skillsArray;
    private ArrayList<String> classesArray;

    private ProfileActivity instance = null;
    private Button editName;
    private Button editGit;
    private Button editLi;
    private Button editSkill;
    private Button editClass;
    private long mLastClickTime = 0;

    //@author: Hugh Leow
    //@brief: Used for API call to retrieve user information from the database
    private GetUserData userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        instance = this;
        profilePic = (ImageView) findViewById(R.id.profilePicture);
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

        //Button edit <x>
        //@author: Hugh Leow
        //@brief:
        //User can click on any of the edit buttons and will be sent to another activity to update their information
        //@pre condition: User is viewing their own profile
        //@post condition: User is sent to a fragment to edit the field they want

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
        // API call to populate the page with updated data
        userDetails = new GetUserData(getApplicationContext(), instance, instance, instance);
        userDetails.getUserData();

        userNickname.invalidate();
        userNickname.requestLayout();
        githubLink.invalidate();
        githubLink.requestLayout();
        linkedinLink.invalidate();
        linkedinLink.requestLayout();
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
                this.finish();
                return true;
            case R.id.collab_Nav:
                Intent collabIntent = new Intent(this, CollabListActivity.class);
                this.startActivity(collabIntent);
                return true;
            case R.id.nav_messages:
                Intent messageIntent = new Intent(this, ConversationsActivity.class);
                this.startActivity(messageIntent);
                return true;
            case R.id.nav_logout:
                GeneralTools.doRestart(this);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //@author: Hugh Leow
    //@brief: User is sent to UserSkillsActivity.java to update their skills
    private void sendUserToSkills() {
        Intent skillPage = new Intent (ProfileActivity.this, UserSkillsActivity.class);
        startActivity(skillPage);
    }

    //@author: Hugh Leow
    //@brief: User is sent to UserClassesActivity.java to update their classes
    private void sendUserToClasses() {
        Intent classPage = new Intent (ProfileActivity.this, UserClassesActivity.class);
        startActivity(classPage);
    }

    //@author: Hugh Leow
    //@brief: User is sent to EditProfileActivity.java with a key to determine what they are editing
    private void sendUserToEditName() {
        Intent editProfile = new Intent (ProfileActivity.this, EditProfileActivity.class);
        Bundle x = new Bundle();
        x.putInt("key",1);
        editProfile.putExtras(x);
        startActivityForResult(editProfile, 1);
    }

    //@author: Hugh Leow
    //@brief: User is sent to EditProfileActivity.java with a key to determine what they are editing
    private void sendUserToEditGithub() {
        Intent editProfile = new Intent (ProfileActivity.this, EditProfileActivity.class);
        Bundle x = new Bundle();
        x.putInt("key",2);
        editProfile.putExtras(x);
        startActivity(editProfile);
    }

    //@author: Hugh Leow
    //@brief: User is sent to EditProfileActivity.java with a key to determine what they are editing
    private void sendUserToEditLinkedIn() {
        Intent editProfile = new Intent (ProfileActivity.this, EditProfileActivity.class);
        Bundle x = new Bundle();
        x.putInt("key",3);
        editProfile.putExtras(x);
        startActivity(editProfile);
    }


    //@author: Hugh Leow
    //@brief:
    //Interface function for ASYNC HTTP request from GetUserData.java
    //If information is successfully received from the database, populate the screen with user information
    //Name
    //Github (converted into a link)
    //LinkedIn (converted into a link)
    //Classes
    //Skills
    //Profile Picture (using Picasso)
    //See: GetUserData.java
    //@params: [Boolean success]
    //@pre condition: User information not retrieved from the database
    //@post condition: User information retrieved if success = 'true'
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

            if (!userDetails.getUserProfileLink().isEmpty()){
                profilePicLink = "https://dkdno63yk5s4u.cloudfront.net/" + userDetails.getUserProfileLink();
            }
            Picasso.get().load(profilePicLink).into(profilePic);

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

    @Override
    public void downloadProfileComplete(Boolean success) {
    }

    @Override
    public void ownerDownloadComplete(Boolean success) {
    }

}
