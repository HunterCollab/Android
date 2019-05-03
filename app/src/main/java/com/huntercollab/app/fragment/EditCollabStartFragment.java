package com.huntercollab.app.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.huntercollab.app.loopjtasks.UpdateCollabData;

import java.util.Calendar;


public class EditCollabStartFragment extends Fragment implements UpdateCollabData.UpdateCollabComplete {

    public EditCollabStartFragment() {
        // Required empty public constructor
    }

    EditCollabTitleFragment.OnDataPass dataPasser;
    private String collabid;
    private EditText date;
    private EditText time;
    private Button editDateButton;
    private Button editTimeButton;
    private Button saveStartButton;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private long dateTimeInMS;

    private EditCollabStartFragment instance = null;
    private UpdateCollabData updateStart;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (EditCollabTitleFragment.OnDataPass) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        collabid = dataPasser.onDataPass();

        // Inflate the layout for this fragment
        instance = this;
        View view = inflater.inflate(R.layout.fragment_edit_collab_start, container, false);
        date = (EditText) view.findViewById(R.id.editDate);
        time = (EditText) view.findViewById(R.id.editTime);
        editDateButton = (Button) view.findViewById(R.id.button_date);
        editTimeButton = (Button) view.findViewById(R.id.button_time);

        // letting user select date and time
        editDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mYear = view.getYear();
                                mMonth = view.getMonth();
                                mDay = view.getDayOfMonth();

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        editTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
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

                                time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        saveStartButton = (Button) view.findViewById(R.id.saveStart);
        saveStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                String newDate = date.getText().toString();
                String newTime = time.getText().toString();

                if (newDate.isEmpty() || newTime.isEmpty()){
                    Toast t = Toast.makeText(getContext(), "Fields cannot be empty.", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                } else {
                    Calendar collabDateTime = Calendar.getInstance();
                    collabDateTime.set(mYear, mMonth, mDay, mHour, mMinute, 0);
                    dateTimeInMS = collabDateTime.getTimeInMillis();
                    long dateTime = dateTimeInMS;

                    //String newStart = Long.toString(dateTime);

                    updateStart = new UpdateCollabData(getContext(), instance);
                    updateStart.updateCollabStartDate(dateTime, collabid);
                    saveStartButton.setEnabled(false);
                }
            }
        });

        return view;
    }

    @Override
    public void updateCollabComplete(Boolean success) {
        if (success) {
            getActivity().finish();
        }
        else {
            // show error message to user
            Toast t = Toast.makeText(getContext(), "ERROR. TRY AGAIN.", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
            saveStartButton.setEnabled(true);
        }
    }
}
