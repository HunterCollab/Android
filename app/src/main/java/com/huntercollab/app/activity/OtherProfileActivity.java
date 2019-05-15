package com.huntercollab.app.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.huntercollab.app.network.loopjtasks.GetUserData;
import com.huntercollab.app.utils.Interfaces;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OtherProfileActivity extends AppCompatActivity implements Interfaces.DownloadComplete, Interfaces.DownloadProfleComplete,
        Interfaces.OwnerDownloadComplete {

    private Context context = OtherProfileActivity.this;
    private TextView userNickname;
    private TextView githubLink;
    private TextView linkedinLink;
    private TextView skills;
    private TextView classes;
    private Button sendUserMessage;
    private ImageView profilePic;

    private String profilePicLink = "https://i.imgur.com/EK6jPjm.png";
    private ArrayList<String> skillsArray;
    private ArrayList<String> classesArray;

    private OtherProfileActivity instance = null;
    private GetUserData userDetails;
    private String memberUsername;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page_of_others);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // grab username from previous activity
        Bundle x = getIntent().getExtras();
        if (x != null) {
            memberUsername = x.getString("memberUsername");
            currentUser = x.getString("currentUser");
        }

        instance = this;
        profilePic = (ImageView) findViewById(R.id.profilePicture);
        userNickname = (TextView) findViewById(R.id.userName);
        githubLink = (TextView) findViewById(R.id.githubLink);
        linkedinLink = (TextView) findViewById(R.id.linkedinLink);
        skills = (TextView) findViewById(R.id.skillsList);
        classes = (TextView) findViewById(R.id.classesList);

        //@author: Hugh Leow
        //@brief: User can send another user a direct message from here
        //@pre condition: User is viewing profile page of another user
        //@post condition: User is sent to a messaging screen for direct messaging between them and the user they are viewing
        sendUserMessage = (Button) findViewById(R.id.send_message_to_user);
        sendUserMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewChat = new Intent(getApplicationContext(), MessagingActivity.class);
                viewChat.putExtra("chatId", memberUsername);
                viewChat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(viewChat);
            }
        });

        if (currentUser.equals(memberUsername))
            sendUserMessage.setVisibility(View.INVISIBLE);

        //@author: Hugh Leow
        //@brief: Used for API call to retrieve user information from the database
        userDetails = new GetUserData(getApplicationContext(), instance, instance, instance);
        userDetails.getOtherUserData(memberUsername);
    }

    // abstract function from GetUserData.java defined here
    // populate profile screen with data on successful API call
    @Override
    public void downloadComplete(Boolean success) {

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
    //@pre condition: Profile data not retrieved for the profile being viewed
    //@post condition: Profile data retrieved if success = 'true'
    @Override
    public void downloadProfileComplete(Boolean success) {
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
    public void ownerDownloadComplete(Boolean success) {

    }

}
