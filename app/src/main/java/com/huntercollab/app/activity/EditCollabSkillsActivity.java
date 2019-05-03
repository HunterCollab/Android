
package com.huntercollab.app.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.huntercollab.app.adapter.AutoCompleteAdapter;
import com.example.socialmediaapp.R;
import com.huntercollab.app.adapter.UserListAdapter;
import com.huntercollab.app.network.loopjtasks.DoSkillSearch;
import com.huntercollab.app.network.loopjtasks.UpdateCollabData;

import java.util.ArrayList;

public class EditCollabSkillsActivity extends AppCompatActivity
        implements DoSkillSearch.OnDoSkillSearchComplete, UpdateCollabData.UpdateCollabComplete {

    private Context context = EditCollabSkillsActivity.this;
    private RecyclerView recyclerView;
    private UserListAdapter mAdapter;
    private ArrayList<String> skillNames;
    private AutoCompleteTextView autoCompleteTextView;
    private EditCollabSkillsActivity instance = null;

    //Variable of our costume adapter that will listen to changes as we search for skills
    private AutoCompleteAdapter adapter;
    private Handler handler;
    final int TRIGGER_AUTO_COMPLETE = 100;
    final long AUTO_COMPLETE_DELAY = 300;
    private DoSkillSearch search;

    private UpdateCollabData updateSkills = null;
    private Button updateSkillsButton = null;

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback;

    private String collabId;
    private ArrayList<String> skillsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_skills);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // grab data from previous activity
        Bundle x = getIntent().getExtras();
        if (x != null) {
            collabId = x.getString("collabId");
            skillsArray = x.getStringArrayList("skillsArray");
        }


        instance = this;

        //skills
        skillNames = new ArrayList<String>();
        skillNames = skillsArray;

///////////////////////////////////// Auto Complete ////////////////////////////////////////////////////////////////
        //Will be used to make the API call
        search = new DoSkillSearch(getApplicationContext(), instance);

        //Maps the skills_auto_complete from the activity_user_skills.xml file to the variable autoCompleteTextView
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.skill_auto_complete);

        Button addSkillButton = (Button) findViewById(R.id.add_skill_button);

        //Creates an adapter using our custom class AutoComplete Adapter
        adapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);

        //Sets the limit on the characters needed to be typed before the adapter displays data or makes an api call
        autoCompleteTextView.setThreshold(1);

        //Changes the list of data used for auto complete. This one uses our custom adapter
        autoCompleteTextView.setAdapter(adapter);

        //This section handles the api calls as we type characters in the autocompleteview section.
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //We only make api calls whenever a character is added or deleted.
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //removes previous message
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                //Will trigger the api call.
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //Will be triggered by onTextChange listener
                if(msg.what == TRIGGER_AUTO_COMPLETE){
                    if(!TextUtils.isEmpty(autoCompleteTextView.getText())){
                        //After the delay, we will make the api call
                        search.doSkillSearch(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

        addSkillButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String skill = autoCompleteTextView.getText().toString();
                        if(skillNames.contains(skill)){
                            Toast t = Toast.makeText(context, "Duplicate. Try again.", Toast.LENGTH_LONG);
                            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                            t.show();
                            mAdapter.notifyDataSetChanged();
                        }
                        else if(skill.length() != 0){
                            skillNames.add(autoCompleteTextView.getText().toString());
                            autoCompleteTextView.getText().clear();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );

//////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////// Filling Recycler View //////////////////////////////////////////////////////////


        /////////////////////////Delete an Item from the Recycler View///////////////////////////////
        itemTouchHelperCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                        //Row is swiped from recycler view
                        //Remove it from adapter
                        int pos = viewHolder.getAdapterPosition();
                        skillNames.remove(pos);
                        mAdapter.notifyItemRemoved(pos);
                        System.out.println("Array: " + skillNames);

                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        //Load the background view
                    }
                };

        recyclerView = (RecyclerView) findViewById(R.id.skill_recycler_view);
        mAdapter = new UserListAdapter(skillNames, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


//////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////Update Skills/////////////////////////////////////////////////////////

        updateSkills = new UpdateCollabData(getApplicationContext(), instance);
        updateSkillsButton = (Button) findViewById(R.id.update_skill);
        updateSkillsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateSkills.updateCollabSkills(skillNames, collabId);
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////////


    }


    @Override
    public void searchSkillComplete(ArrayList<String> message) {
        //Sets the new data as we retrieve new suggestions from the
        adapter.setData(message);
        //Once the new data is set, notify the adapter and show the new data.
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateCollabComplete(Boolean success) {
        if (success) {
            Toast t = Toast.makeText(context, "Update complete.", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
        } else {
            Toast t = Toast.makeText(context, "SERVER ERROR.", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
        }

    }

}