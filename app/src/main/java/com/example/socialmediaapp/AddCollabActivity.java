package com.example.socialmediaapp;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Toast;

import com.example.socialmediaapp.loopjtasks.GetCollabsData;

import java.util.ArrayList;
import java.util.Calendar;

public class AddCollabActivity extends AppCompatActivity implements View.OnClickListener, GetCollabsData.GetCollabDataComplete, GetCollabsData.AddCollabComplete {

    // TODO: LET USER REMOVE SKILLS/CLASSES WHILE ADDING COLLAB (EDWIN)
    // TODO: LET USER SET DURATION, NOT DEFAULT (WAITING FOR ARIEL)

    private Context context = AddCollabActivity.this;
    private EditText collabName;
    private EditText collabLocation;
    private EditText collabDescription;
    private EditText txtDate;
    private EditText txtTime;
    private EditText skillInput;
    private EditText classInput;
    private EditText collabSize;
    private EditText collabDuration;
    private TextView skillsView;
    private TextView classesView;
    private Button btnDatePicker;
    private Button btnTimePicker;
    private Button confirmAddCollab;
    private Button addSkillButton;
    private Button addClassButton;
    private ArrayList<String> skillsArray = new ArrayList<String>();
    private ArrayList<String> classesArray = new ArrayList<String>();
    private long mLastClickTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private long dateTimeInMS;

    private GetCollabsData addCollab = null;
    private AddCollabActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collab);

        instance = this;

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
        collabSize = (EditText) findViewById(R.id.collabSize);
        collabDescription = (EditText) findViewById(R.id.collab_description);
        collabDuration = (EditText) findViewById(R.id.collab_duration);
        skillInput = (EditText) findViewById(R.id.wantedSkills);
        classInput = (EditText) findViewById(R.id.wantedClasses);
        addSkillButton = (Button) findViewById(R.id.btn_skill);
        addClassButton = (Button) findViewById(R.id.btn_class);
        skillsView = (TextView) findViewById(R.id.skillView);
        classesView = (TextView) findViewById(R.id.classView);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        addSkillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getInput = skillInput.getText().toString();
                if (skillsArray.contains(getInput)){
                    Toast t = Toast.makeText(context, "Duplicate.", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }
                else if (getInput.trim().equals("")){
                    Toast t = Toast.makeText(context, "Empty field.  Try again.", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }
                else {
                    skillsArray.add(getInput);
                    skillsView.append(getInput + "\n");
                    skillInput.getText().clear();
                    }
            }
        });

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getInput = classInput.getText().toString();
                if (classesArray.contains(getInput)){
                    Toast t = Toast.makeText(context, "Duplicate.", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }
                else if (getInput.trim().equals("")){
                    Toast t = Toast.makeText(context, "Empty field.  Try again.", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }
                else {
                    classesArray.add(getInput);
                    classesView.append(getInput + "\n");
                    classInput.getText().clear();
                }
            }
        });

        // build JSON and send HTTP request
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
                String CollabSize = collabSize.getText().toString();
                String CollabDuration = collabDuration.getText().toString();

                // converting String to int with catch exception
                Integer collabSizeInt = 0;
                try {
                    collabSizeInt = Integer.parseInt(CollabSize);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                // get duration int, and convert to ms
                long collabDurationLong = 0;
                try {
                    collabDurationLong = Long.parseLong(CollabDuration);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                collabDurationLong *= 86400000;

                // if any fields are empty or collab size is 0, tell user, otherwise call API request
                if (CollabName.isEmpty() || CollabLocation.isEmpty() || CollabDescription.isEmpty() || CollabSize.isEmpty() ||
                        skillsArray.isEmpty() || classesArray.isEmpty() || CollabDate.isEmpty() || CollabTime.isEmpty()) {
                    Toast t = Toast.makeText(context, "Fields cannot be empty.", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }
                else if (CollabSize.equals("0")) {
                    Toast t = Toast.makeText(context, "Collab size cannot be 0.", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }
                else {
                    // get date/time as milliseconds from datepicker and timepicker
                    Calendar collabDateTime = Calendar.getInstance();
                    collabDateTime.set(mYear, mMonth, mDay, mHour, mMinute, 0);
                    dateTimeInMS = collabDateTime.getTimeInMillis();
                    long dateTime = dateTimeInMS;
                    confirmAddCollab.setEnabled(false);

                    // call the API
                    addCollab = new GetCollabsData(getApplicationContext(), instance, instance);
                    addCollab.addCollab(CollabName, CollabLocation, CollabDescription, collabSizeInt, skillsArray, classesArray, dateTime, collabDurationLong);
                }
            }
        });
    }

    // letting user select date and time

    @Override
    @SuppressWarnings("deprecation")
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mYear = view.getYear();
                            mMonth = view.getMonth();
                            mDay = view.getDayOfMonth();

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
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            if (Build.VERSION.SDK_INT >= 23){
                                mHour = view.getHour();
                                mMinute = view.getMinute();
                            }
                            else {
                                mHour = view.getCurrentHour();
                                mMinute = view.getCurrentMinute();
                            }

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }


    @Override
    public void getAllCollabs(Boolean success) {

    }

    @Override
    public void addCollabComplete (Boolean success) {
        if (success) {
            finish();
        }
        else {
            // show error message to user
            Toast t = Toast.makeText(getApplicationContext(), "ERROR. TRY AGAIN.", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
            confirmAddCollab.setEnabled(true);
        }
    }
}
