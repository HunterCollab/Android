package com.example.socialmediaapp;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.loopjtasks.CollabModel;
import com.example.socialmediaapp.loopjtasks.JoinDropCollab;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * A fragment representing a single Collab detail screen.
 * This fragment is either contained in a {@link CollabListActivity}
 * in two-pane mode (on tablets) or a {@link CollabDetailActivity}
 * on handsets.
 */
public class CollabDetailFragment extends Fragment implements JoinDropCollab.JoinComplete, JoinDropCollab.LeaveComplete {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private CollabModel mItem;
    private CollabDetailFragment instance = null;
    private TextView collabDateTime;
    private TextView collabLocation;
    private TextView collabSkills;
    private TextView collabClasses;
    private TextView collabMembers;

    private Button joinCollab;
    private JoinDropCollab doJoinCollab;

    private Button leaveCollab;
    private JoinDropCollab doLeaveCollab;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public CollabDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = CollabContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            mItem = new CollabModel(Parcel.obtain());

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getArguments().getString("title"));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.collab_detail, container, false);

        instance = this;

        // Show the content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.collab_detail)).setText(getArguments().getString("description"));
            collabLocation = (TextView) rootView.findViewById(R.id.collab_Location_Info);
            collabLocation.setText(getArguments().getString("location"));

            // populate skills
            collabSkills = (TextView) rootView.findViewById(R.id.collab_Skills_Request_Info);
            collabSkills.setText("");
            ArrayList<String> skillsArray = getArguments().getStringArrayList("skills");
            if (skillsArray != null){
                int len = skillsArray.size();
                for (int i = 0; i < len; i++){
                    collabSkills.append(skillsArray.get(i) + "\n");
                }
            }

            // populate classes
            collabClasses = (TextView) rootView.findViewById(R.id.collab_Classes_Request_Info);
            collabClasses.setText("");
            ArrayList<String> classesArray = getArguments().getStringArrayList("classes");
            if (classesArray != null){
                int len = classesArray.size();
                for (int i = 0; i < len; i++){
                    collabClasses.append(classesArray.get(i) + "\n");
                }
            }

            // populate members
            collabMembers = (TextView) rootView.findViewById(R.id.collab_Members_Info);
            collabMembers.setText("");
            ArrayList<String> membersArray = getArguments().getStringArrayList("members");
            if (membersArray != null){
                int len = membersArray.size();
                for (int i = 0; i < len; i++){
                    collabMembers.append(membersArray.get(i) + "\n");
                }
            }

            collabDateTime = (TextView) rootView.findViewById(R.id.collab_DateTime_Info);

            long dateInMilli = getArguments().getLong("date");
            DateFormat convert = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            //convert.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date result = new Date(dateInMilli);

            collabDateTime.setText(convert.format(result));

            // TODO: SHOW USER NICKNAMES (NOT USER NAMES)

            joinCollab = (Button) rootView.findViewById(R.id.join_collab_button);
            joinCollab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String newGithub = editGithub.getText().toString();
                    //updateGithub = new SetUserData(getContext(), instance);
                    //updateGithub.setUserGithub(newGithub);
                    //getActivity().finish();
                    doJoinCollab = new JoinDropCollab(getContext(), instance, instance);
                    String collabId = getArguments().getString("collabId");
                    doJoinCollab.joinCollab(collabId);
                }
            });

            leaveCollab = (Button) rootView.findViewById(R.id.leave_collab_button);
            leaveCollab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doLeaveCollab = new JoinDropCollab(getContext(), instance, instance);
                    String collabId = getArguments().getString("collabId");
                    doLeaveCollab.leaveCollab(collabId);
                }
            });


        }

        return rootView;
    }

    @Override
    public void joinComplete(Boolean success) {
        if(success){
            CharSequence text = "You have joined the collab!";
            System.out.println("text: " + text);

            Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            CharSequence text = "Cannot join!";
            System.out.println("text: " + text);

            Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    @Override
    public void leaveComplete(Boolean success) {
        if(success){
            CharSequence text = "You have left the collab!";
            System.out.println("text: " + text);

            Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            CharSequence text = "Cannot leave!";
            System.out.println("text: " + text);

            Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
            toast.show();
        }

    }
}
