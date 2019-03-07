package com.example.socialmediaapp;

import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;

import com.example.socialmediaapp.loopjtasks.DoLogin;

import java.util.Calendar;

public class AddCollabActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText collabName;
    private EditText collabLocation;
    private EditText collabDescription;
    private EditText txtDate;
    private EditText txtTime;
    private Button btnDatePicker;
    private Button btnTimePicker;
    private Button confirmAddCollab;
    private long mLastClickTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        confirmAddCollab = (Button) findViewById(R.id.addCollab_button);
        btnDatePicker= (Button) findViewById(R.id.btn_date);
        btnTimePicker= (Button) findViewById(R.id.btn_time);
        txtDate= (EditText) findViewById(R.id.in_date);
        txtTime= (EditText) findViewById(R.id.in_time);
        collabName = (EditText) findViewById(R.id.collab_name);
        collabLocation = (EditText) findViewById(R.id.collab_location);
        collabDescription = (EditText) findViewById(R.id.collab_description);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        confirmAddCollab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Variables from xml
                // if user clicks button again in less than 3 seconds, it will not work
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000)
                    return;
                mLastClickTime = SystemClock.elapsedRealtime();
                String CollabName = collabName.getText().toString();
                String CollabLocation = collabLocation.getText().toString();
                String CollabDescription = collabDescription.getText().toString();
                String CollabDate = txtDate.getText().toString();
                String CollabTime = txtTime.getText().toString();
                // TODO: ADD COLLAB (POST REQUEST)
                finish();
            }
        });
    }

    // letting user select date and time
    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}
