package com.example.socialmediaapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Property;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.loopjtasks.GetUserData;
import com.example.socialmediaapp.loopjtasks.SetUserData;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class UserSkillsActivity extends AppCompatActivity implements SetUserData.OnSkillUpdateComplete {
    final int TRIGGER_AUTO_COMPLETE = 100;
    final long AUTO_COMPLETE_DELAY = 300;

    private Handler handler;
    //Variable of our costume adapter that will listen to changes as we search for skills
    private AutoCompleteAdapter adapter;
    //List of arrays that will store both the skills retrieved from the server and the new ones added.
    private ArrayList<String> skillNames = null;

    private AutoCompleteTextView autoCompleteTextView = null;

    private SwipeMenuListView listView;

    //private ArrayAdapter<String> skillAdapter = null;

    private Button updateSkillsButton = null;

    private SetUserData updateSkills = null;

    private UserSkillsActivity instance = null;

    private  ArrayAdapter<String> skillAdapter = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_skills);
        instance = this;

////////////////////////////////////Update Skills/////////////////////////////////////////////////////////

        updateSkills = new SetUserData(getApplicationContext(), instance);
        updateSkillsButton = (Button) findViewById(R.id.update_skills);
        updateSkillsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateSkills.setUserSkills(skillNames);
                CharSequence message = "Skills Updated";
                Toast t = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                t.show();
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////Display Skills/////////////////////////////////////////////////////////



        //skills
        skillNames = new ArrayList<String>();
        GetUserData userData = new GetUserData(getApplicationContext());
        userData.getUserData();
        skillNames = userData.getUserSkills();

        //Custom adapter implementation
        skillAdapter = new skillArrayAdapter(this,  0,skillNames);
        //

        skillAdapter.notifyDataSetChanged();
        listView = (SwipeMenuListView) findViewById(R.id.all_skills);
        //skillAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, skillNames);
        listView.setAdapter(skillAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.skill_remove_icon);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        skillNames.remove(position);
                        skillAdapter.notifyDataSetChanged();
                        break;
                }
                // false : close the menu; true : not close the menu
                return true;
            }
        });

/////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////// Auto Complete ////////////////////////////////////////////////////////////////
        //Maps the skills_auto_complete from the activity_user_skills.xml file to the variable autoCompleteTextView
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.skills_auto_complete);

        Button addSkillButton = (Button) findViewById(R.id.add_skill_button);

        //Creates an adapter using our custom class AutoComplete Adapter
        adapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);

        //Sets the limit on the characters needed to be typed before the adapter displays data or makes an api call
        autoCompleteTextView.setThreshold(1);

        //Changes the list of data used for auto complete. This one uses our custom adapter
        autoCompleteTextView.setAdapter(adapter);

        //Once we find a skill that we want to add to our list, we can click on it. This section handles that.
        //It also adds what we click on to our recycler view.
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //skillNames.add(adapter.getItem(position));

                        //This will add the autocompleted skill to the textView
                        autoCompleteTextView.setText(adapter.getItem(position));
                        skillAdapter.notifyDataSetChanged();
                    }
                });

        addSkillButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String skill = autoCompleteTextView.getText().toString();
                        if(skill.length() != 0){
                            skillNames.add(autoCompleteTextView.getText().toString());
                            skillAdapter.notifyDataSetChanged();
                        } else {
                            skillAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );

        //This section handles the api calls as we type characters in the autocompleteview section.
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //We only make api calls whenever a character is added or deleted.
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                skillAdapter.notifyDataSetChanged();
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
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });


    }

////////////////////////Local Function for Calling API to ///////////////////////////////

    private void makeApiCall(String constraint) {

        AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(this);
        final RequestParams requestParams = new RequestParams();
        requestParams.put("query", constraint);

        asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/search/skills", requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                List<String> stringList = new ArrayList<>();

                System.out.println("Response: " + response);

                try {
                    JSONArray terms = null;
                    terms = response.getJSONArray("matches");

                    for(int i=0; i < terms.length(); i++){
                        String term = terms.getString(i);
                        stringList.add(term);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Sets the new data as we retrieve new suggestions from the
                adapter.setData(stringList);
                //Once the new data is set, notify the adapter and show the new data.
                adapter.notifyDataSetChanged();


            }
        });


    }

    @Override
    public void skillUpdateComplete(Boolean success, String message) {
        System.out.println(message);
    }


    //Custom ArrayAdapter
    public class skillArrayAdapter extends ArrayAdapter<String>{

        Context mContext;
        ArrayList<String> userData;

        //Constructor used to create an instance of the object.
        public skillArrayAdapter(Context context, int resource, ArrayList<String> objects) {
            super(context,resource, objects);
            this.mContext = context;
            this.userData = objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

                View v = convertView;

                if(v == null) {
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.user_data_layout, null);
                    ViewHolder holder = new ViewHolder();
                    holder.txtName = (TextView) v.findViewById(R.id.data_name);
                    v.setTag(holder);
                }


                String skill = userData.get(position);
                if(skill != null){
                    ViewHolder holder = (ViewHolder)v.getTag();
                    holder.txtName.setText(skill);
                  }

                return v;

        }
    }

    static class ViewHolder {
        TextView txtName;
    }
}