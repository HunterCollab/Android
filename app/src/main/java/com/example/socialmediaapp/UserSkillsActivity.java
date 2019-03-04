package com.example.socialmediaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import com.example.socialmediaapp.loopjtasks.AutoCompleteAdapter;

public class UserSkillsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_skills);

       final AutoCompleteTextView style = (AutoCompleteTextView) findViewById(R.id.skills_auto_complete);
        final AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);
        style.setThreshold(1);
        style.setAdapter(adapter);



    }
}
