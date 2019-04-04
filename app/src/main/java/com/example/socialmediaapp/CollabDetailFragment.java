package com.example.socialmediaapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcel;
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
import com.example.socialmediaapp.loopjtasks.GetUserData;
import com.example.socialmediaapp.loopjtasks.JoinDropCollab;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A fragment representing a single Collab detail screen.
 * This fragment is either contained in a {@link CollabListActivity}
 * in two-pane mode (on tablets) or a {@link CollabDetailActivity}
 * on handsets.
 */
public class CollabDetailFragment extends Fragment implements JoinDropCollab.JoinComplete, JoinDropCollab.LeaveComplete,
        JoinDropCollab.EditComplete, JoinDropCollab.DeleteComplete, GetUserData.DownloadComplete, GetUserData.DownloadProfleComplete,
        GetUserData.OwnerDownloadComplete {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    // TODO: IMPLEMENT EDIT COLLAB FOR OWNER (ME)

    public static final String ARG_ITEM_ID = "item_id";

    private String currentUsername;
    private CollabModel mItem;
    private CollabDetailFragment instance = null;
    private TextView collabOwner;
    private TextView collabStartDateTime;
    private TextView collabEndDateTime;
    private TextView collabLocation;
    private TextView collabSkills;
    private TextView collabClasses;
    private TextView collabMembers;

    private Button viewMembers;

    private Button joinCollab;
    private JoinDropCollab doJoinCollab;

    private Button leaveCollab;
    private JoinDropCollab doLeaveCollab;

    private Button editCollab;
    private JoinDropCollab doEditCollab;

    private Button deleteCollab;
    private JoinDropCollab doDeleteCollab;

    private GetUserData userDetails;
    private GetUserData memberDetails;
    private GetUserData ownerDetails;
    private ArrayList<String> membersArray;
    private ArrayList<String> classesArray;
    private ArrayList<String> membersArrayForRecyclerView = new ArrayList<>();
    private ArrayList<String> membersArrayNicknames = new ArrayList<>();

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

        userDetails = new GetUserData(getContext(), instance, instance, instance);
        memberDetails = new GetUserData(getContext(), instance, instance, instance);
        ownerDetails = new GetUserData(getContext(), instance, instance, instance);
        userDetails.getUserData();

        // Show the content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.collab_detail)).setText(getArguments().getString("description"));
            collabLocation = (TextView) rootView.findViewById(R.id.collab_Location_Info);
            collabLocation.setText(getArguments().getString("location"));
            collabOwner = (TextView) rootView.findViewById(R.id.collab_Owner_Info);
            //collabOwner.setText(null);
            //collabOwner.setText(getArguments().getString("owner"));
            ownerDetails.getOwnerUserData(getArguments().getString("owner"));

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
            classesArray = getArguments().getStringArrayList("classes");
            if (classesArray != null){
                int len = classesArray.size();
                for (int i = 0; i < len; i++){
                    collabClasses.append(classesArray.get(i) + "\n");
                }
            }

            // populate members
            collabMembers = (TextView) rootView.findViewById(R.id.collab_Members_Info);
            collabMembers.setText("");
            membersArray = getArguments().getStringArrayList("members");
            if (membersArray != null){
                membersArrayNicknames.clear();
                int len = membersArray.size();
                for (int i = 0; i < len; i++){
                    memberDetails.getOtherUserData(membersArray.get(i));
                }
            }

            // populate start time
            collabStartDateTime = (TextView) rootView.findViewById(R.id.collab_StartDateTime_Info);
            long dateInMilli = getArguments().getLong("date");
            DateFormat convert = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            //convert.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date result = new Date(dateInMilli);
            collabStartDateTime.setText(convert.format(result));

            // populate end time (duration)
            collabEndDateTime = (TextView) rootView.findViewById(R.id.collab_EndDateTime_Info);
            long endDateInMilli = getArguments().getLong("duration");
            DateFormat convert1 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            //convert1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date result1 = new Date(endDateInMilli);
            collabEndDateTime.setText(convert1.format(result1));

            // view members button
            viewMembers = (Button) rootView.findViewById(R.id.viewMembers_button);
            viewMembers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewMembersIntent = new Intent(getContext(), ViewMembersOfCollabActivity.class);
                    viewMembersIntent.putExtra("membersList", membersArrayForRecyclerView);
                    viewMembersIntent.putExtra("membersListNicknames", membersArrayNicknames);
                    viewMembersIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(viewMembersIntent);
                }
            } );

            // join button
            joinCollab = (Button) rootView.findViewById(R.id.join_collab_button);
            joinCollab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        doJoinCollab = new JoinDropCollab(getContext(), instance, instance, instance, instance);
                        String collabId = getArguments().getString("collabId");
                        doJoinCollab.joinCollab(collabId);
                        joinCollab.setEnabled(false);
                }
            });

            // edit button
            editCollab = (Button) rootView.findViewById(R.id.edit_collab_button);
            editCollab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send to edit collab screen
                }
            });

            // leave button
            leaveCollab = (Button) rootView.findViewById(R.id.leave_collab_button);
            leaveCollab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // dialog box asking user to confirm leave/delete if they're the last member
                    if (membersArray.size() == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Confirm");
                        builder.setMessage("You are the last member, this collaboration will be deleted, are you sure?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // delete the collab on yes click
                                doDeleteCollab = new JoinDropCollab(getContext(), instance, instance, instance, instance);
                                String collabId = getArguments().getString("collabId");
                                doDeleteCollab.deleteCollab(collabId);
                                Intent collabIntent = new Intent(getContext(), CollabListActivity.class);
                                collabIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(collabIntent);
                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing on no click
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        doLeaveCollab = new JoinDropCollab(getContext(), instance, instance, instance, instance);
                        String collabId = getArguments().getString("collabId");
                        doLeaveCollab.leaveCollab(collabId);
                        leaveCollab.setEnabled(false);
                    }
                }
            });

            // delete button
            deleteCollab = (Button) rootView.findViewById(R.id.delete_collab_button);
            deleteCollab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // dialog box asking user to confirm deletion
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // delete the collab on yes click
                            doDeleteCollab = new JoinDropCollab(getContext(), instance, instance, instance, instance);
                            String collabId = getArguments().getString("collabId");
                            doDeleteCollab.deleteCollab(collabId);
                            Intent collabIntent = new Intent(getContext(), CollabListActivity.class);
                            collabIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(collabIntent);
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing on no click
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

        }

        return rootView;
    }

    // interfaces for API success/fail
    @Override
    public void joinComplete(Boolean success, String message) {
        if(success){
            Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
            toast.show();

            joinCollab.setVisibility(View.INVISIBLE);
            leaveCollab.setVisibility(View.VISIBLE);
            joinCollab.setEnabled(true);

            // add member locally
            membersArray.add(userDetails.getUserName());
            membersArrayForRecyclerView.add(userDetails.getUserName());
            membersArrayNicknames.add(userDetails.getUserNickname());
            collabMembers.append(userDetails.getUserNickname() + "\n");
        } else {
            Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
            toast.show();
            joinCollab.setEnabled(true);
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

            leaveCollab.setVisibility(View.INVISIBLE);
            joinCollab.setVisibility(View.VISIBLE);
            editCollab.setVisibility(View.INVISIBLE);
            deleteCollab.setVisibility(View.INVISIBLE);
            leaveCollab.setEnabled(true);

            // repopulate members field locally
            membersArray.remove(userDetails.getUserName());
            membersArrayForRecyclerView.remove(userDetails.getUserName());
            membersArrayNicknames.remove(userDetails.getUserNickname());
            collabMembers.setText("");
            if (membersArrayNicknames != null){
                int len = membersArrayNicknames.size();
                for (int i = 0; i < len; i++){
                    collabMembers.append(membersArrayNicknames.get(i) + "\n");
                }
            }
        } else {
            CharSequence text = "Cannot leave!  Try again.";
            System.out.println("text: " + text);

            Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
            toast.show();
            leaveCollab.setEnabled(true);
        }
    }

    @Override
    public void editComplete(Boolean success) {

    }

    @Override
    public void deleteComplete(Boolean success) {

    }

    // abstract function from GetUserData.java defined here
    @Override
    public void downloadComplete(Boolean success) {
        if (success) {
            currentUsername = userDetails.getUserName();

            // if user is IN collab, show leave, else show join
            if (membersArray.contains(currentUsername)){
                leaveCollab.setVisibility(View.VISIBLE);
            }
            else {
                joinCollab.setVisibility(View.VISIBLE);
            }

            // if user is the owner, show edit/delete collab button
            if (getArguments().getString("owner").equals(userDetails.getUserName())){
                editCollab.setVisibility(View.VISIBLE);
                deleteCollab.setVisibility(View.VISIBLE);
            }
        } else {
            // show error message to user
            Toast t = Toast.makeText(getContext(), "ERROR", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
        }
    }

    @Override
    public void downloadProfileComplete(Boolean success) {
        if (success) {
            membersArrayForRecyclerView.add(memberDetails.getUserName());
            membersArrayNicknames.add(memberDetails.getUserNickname());
            collabMembers.append(memberDetails.getUserNickname() + "\n");
        }
    }

    @Override
    public void ownerDownloadComplete(Boolean success) {
        if (success) {
            collabOwner.setText(ownerDetails.getUserNickname());
        }
    }

}
