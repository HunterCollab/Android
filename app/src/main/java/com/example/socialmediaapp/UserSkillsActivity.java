package com.example.socialmediaapp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.loopjtasks.GetUserData;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class UserSkillsActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerViewAdapter";
    final int TRIGGER_AUTO_COMPLETE = 100;
    final long AUTO_COMPLETE_DELAY = 300;

    private Handler handler;
    //Variable of our costume adapter that will listen to changes as we search for skills
    private AutoCompleteAdapter adapter;
    //List of arrays that will store both the skills retrieved from the server and the new ones added.
    private ArrayList<String> skillNames;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_skills);
        //final TextView selectedText = findViewById(R.id.selected_item);
        skillNames = new ArrayList<String>();

////////////////////////////////////Display Skills/////////////////////////////////////////////////////////

        //skills

        GetUserData userData = new GetUserData(getApplicationContext());
        userData.getUserData();
        skillNames = userData.getUserSkills();
        Log.d(TAG, "onCreate: started");
        initRecyclerView();

/////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////// Auto Complete /////////////////////////////////////////////////
        //Maps the skills_auto_complete from the activity_user_skills.xml file to the variable autoCompleteTextView
       final AutoCompleteTextView autoCompleteTextView =
               (AutoCompleteTextView) findViewById(R.id.skills_auto_complete);

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
                        skillNames.add(adapter.getItem(position));
                        //selectedText.setText(adapter.getItem(position));
                    }
                });

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
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });


    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initSkillsBitmap(){
        Log.d(TAG, "onCreate: preparing bit maps");

    }

    private void initRecyclerView(){
        Log.d(TAG, "onCreate: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.all_skills);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(skillNames,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////

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

                    System.out.println("here");
                    for(int i=0; i < terms.length(); i++){
                        String term = terms.getString(i);
                        System.out.println("term: " + term);
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
}
