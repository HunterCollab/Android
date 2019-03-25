package com.example.socialmediaapp;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialmediaapp.loopjtasks.CollabModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A fragment representing a single Collab detail screen.
 * This fragment is either contained in a {@link CollabListActivity}
 * in two-pane mode (on tablets) or a {@link CollabDetailActivity}
 * on handsets.
 */
public class CollabDetailFragment extends Fragment {
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
            collabDateTime = (TextView) rootView.findViewById(R.id.collab_DateTime_Info);

            // retrieve date from string
            // TODO: FIX FORMAT OF DATE
            Date collabTime = null;
            String dateString;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                collabTime = sdf.parse(getArguments().getString("date"));
            } catch (Exception e) {
                e.getStackTrace();
            }
            dateString = sdf.format(collabTime);
            collabDateTime.setText(dateString);
        }

        return rootView;
    }
}
