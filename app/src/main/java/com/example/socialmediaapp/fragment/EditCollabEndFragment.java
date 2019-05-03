package com.example.socialmediaapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.loopjtasks.UpdateCollabData;


public class EditCollabEndFragment extends Fragment implements UpdateCollabData.UpdateCollabComplete {

    public EditCollabEndFragment() {
        // Required empty public constructor
    }

    EditCollabTitleFragment.OnDataPass dataPasser;
    private String collabid;
    private EditText editDuration;
    private Button saveDurationButton;
    private EditCollabEndFragment instance = null;
    private UpdateCollabData updateDuration;

    private boolean allowRefresh = true;

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
        View view = inflater.inflate(R.layout.fragment_edit_collab_duration, container, false);
        editDuration = (EditText) view.findViewById(R.id.editText);
        saveDurationButton = (Button) view.findViewById(R.id.saveDuration);
        saveDurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get duration int, and convert to ms to multiply and convert back to string
                String newDuration = editDuration.getText().toString();
                long collabDurationLong = 0;
                try {
                    collabDurationLong = Long.parseLong(newDuration);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                collabDurationLong *= 86400000;
                //newDuration = Long.toString(collabDurationLong);
                updateDuration = new UpdateCollabData(getContext(), instance);
                updateDuration.updateCollabEndDate(collabDurationLong, collabid);
                saveDurationButton.setEnabled(false);
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
            saveDurationButton.setEnabled(true);
        }
    }
}
