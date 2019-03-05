package com.example.socialmediaapp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.loopjtasks.AutoCompleteAdapter;
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
    final int TRIGGER_AUTO_COMPLETE = 100;
    final long AUTO_COMPLETE_DELAY = 300;
    Handler handler;
    AutoCompleteAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_skills);
        final TextView selectedText = findViewById(R.id.selected_item);



       final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.skills_auto_complete);
        adapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedText.setText(adapter.getItem(position));
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
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
                if(msg.what == TRIGGER_AUTO_COMPLETE){
                    if(!TextUtils.isEmpty(autoCompleteTextView.getText())){
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

    }

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
                adapter.setData(stringList);
                adapter.notifyDataSetChanged();
            }
        });


    }
}
