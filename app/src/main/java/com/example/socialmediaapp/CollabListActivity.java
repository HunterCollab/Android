package com.example.socialmediaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.loopjtasks.CollabModel;
import com.example.socialmediaapp.loopjtasks.GetCollabsData;
import com.example.socialmediaapp.loopjtasks.GetUserData;
import com.example.socialmediaapp.tools.GeneralTools;

import java.util.ArrayList;

// TODO: FILTER COLLAB (DONE BUT RECOMMEND SHOULD BE FIXED ON BACKEND)

/**
 * An activity representing a list of Collabs. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CollabDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CollabListActivity extends AppCompatActivity
        implements GetCollabsData.GetCollabDataComplete, AdapterView.OnItemSelectedListener, GetCollabsData.AddCollabComplete,
        GetUserData.DownloadComplete, GetUserData.DownloadProfleComplete, GetUserData.OwnerDownloadComplete{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private GetCollabsData collabsClass;
    private CollabListActivity instance;
    public ArrayList<CollabModel> listOfCollabs;
    public CollabModel errorHandler;

    private GetUserData userDetails;
    private ArrayList<String> skillsArray;
    private ArrayList<String> classesArray;

    //spinner for dropdown
    private Spinner spinner;
    private static final String[] paths = {"collabs", "active collabs"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_list);

        errorHandler = new CollabModel();
        instance = this;

        collabsClass = new GetCollabsData(getApplicationContext(), instance, instance);
        userDetails = new GetUserData(getApplicationContext(), instance, instance, instance);
        userDetails.getUserData();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sent to add collab screen
                Context context = view.getContext();
                Intent intent = new Intent(context, AddCollabActivity.class);

                context.startActivity(intent);
            }
        });

        if (findViewById(R.id.collab_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


    }

    @Override
    public void onResume(){
        super.onResume();
        //This will set up the spinner
        //Depending on tne item selected, a different function is called
        spinner = (Spinner) findViewById(R.id.my_spinner);
        //Makes a spinnerAdapter with our custom field and list of array located in strings
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(CollabListActivity.this,
                        R.layout.custom_spinner, getResources().getStringArray(R.array.collabs));

        //Creates a dropdown using a default template
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //Attaches adapter to the spinner
        spinner.setAdapter(spinnerAdapter);
        //Creates a listener to see when the spinner is changed
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    // get ALL collabs in the database
                    // This one gets called at start up by default
                    case 0:
                        if(listOfCollabs != null){
                            listOfCollabs.clear();
                        }
                        collabsClass.getCollabs("getAllCollabs");
                        break;
                    // get collabs USER is a part of
                    case 1:
                        if(listOfCollabs != null){
                            listOfCollabs.clear();
                        }
                        collabsClass.getCollabs("getCollabDetails");
                        break;
                    // get collab recommendations for user
                    case 2:
                        if(listOfCollabs != null){
                            listOfCollabs.clear();
                        }
                        //collabsClass.getCollabs("getRecommendedCollabs");
                        collabsClass.getCollabs(skillsArray, classesArray);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                this.finish();
                return true;
            case R.id.nav_messages:
                Intent messageIntent = new Intent(this, ViewMessagesActivity.class);
                this.startActivity(messageIntent);
                return true;
            case R.id.nav_logout:
                GeneralTools.doRestart(this);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, listOfCollabs));
    }

    @Override
    public void getAllCollabs(Boolean success) {
        if(success){
            listOfCollabs = collabsClass.returnCollabs();
            if (listOfCollabs != null){
                View recyclerView = findViewById(R.id.collab_list);
                assert recyclerView != null;
                setupRecyclerView((RecyclerView) recyclerView);
            }
            // if user collabs is empty
            else {
                Toast t = Toast.makeText(getApplicationContext(), "Nothing to show.", Toast.LENGTH_LONG);
                t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                t.show();
            }
        } else {
            listOfCollabs.clear();
            listOfCollabs.add(errorHandler);

            // show error message to user
            Toast t = Toast.makeText(getApplicationContext(), "Error.  Could not retrieve data.", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();

        }

    }

    @Override
    public void addCollabComplete(Boolean success) {

    }

    //Sinner functions
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // abstract function from GetUserData.java defined here
    // populate profile screen with data on successful API call
    @Override
    public void downloadComplete(Boolean success) {
        skillsArray = userDetails.getUserSkills();
        classesArray = userDetails.getUserClasses();
    }

    @Override
    public void downloadProfileComplete(Boolean success) {
    }

    @Override
    public void ownerDownloadComplete(Boolean success) {
    }


//HERE'S WHERE WE PLACE THE COSTUME VIEW FOR OUR RECYCLER VIEW ADAPTER
//SIMILAR TO OUR ADAPTER FOR SKILLS AND CLASSES
//FRAGMENT IS CALLED FROM HERE

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private static final String TAG = "RecyclerViewAdapter";

        private final CollabListActivity mParentActivity;
        private ArrayList<CollabModel> listOfCollabs;



        SimpleItemRecyclerViewAdapter(CollabListActivity parent,
                                      ArrayList<CollabModel> collabData) {
            mParentActivity = parent;
            listOfCollabs = collabData;
        }



        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollabModel item = (CollabModel) view.getTag();

                //This part will display the CollabDetailFragment
                Context context = view.getContext();
                Intent intent = new Intent(context, CollabDetailActivity.class);
                System.out.println("ID: " + item.id);
                intent.putExtra("collab", listOfCollabs.get(item.id));

                context.startActivity(intent);

            }
        };

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.collab_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: called.");

            holder.mIdView.setText(listOfCollabs.get(position).getTitle());
            holder.mContentView.setText(listOfCollabs.get(position).getDescription());

            ArrayList<String> membersArray = listOfCollabs.get(position).getMembers();
            Integer sizeOfCollab = listOfCollabs.get(position).getSize();
            Integer slotsOpen = sizeOfCollab-membersArray.size();
            if (!sizeOfCollab.equals(membersArray.size())){
                holder.mSlotsOpen.setText(slotsOpen + "/" + sizeOfCollab + " open slots");
            } else {
                holder.mSlotsOpen.setText("FULL");
            }

            holder.detailsButton.setTag(listOfCollabs.get(position));
            holder.detailsButton.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return listOfCollabs.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final TextView mSlotsOpen;
            final Button detailsButton;
            RelativeLayout parentLayout;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.collab_title);
                mContentView = (TextView) view.findViewById(R.id.collab_description);
                mSlotsOpen = (TextView) view.findViewById(R.id.slots_open);
                detailsButton = (Button) view.findViewById(R.id.collab_details_button);
                parentLayout = itemView.findViewById(R.id.collab_main_layout);
            }
        }
    }
}