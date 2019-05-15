package com.huntercollab.app.fragment;

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
import com.huntercollab.app.network.loopjtasks.SetUserData;


public class EditLinkedInFragment extends Fragment implements SetUserData.UpdateComplete {

    public EditLinkedInFragment() {
        // Required empty public constructor
    }

    private EditText editLinkedIn;
    private Button saveLinkedInButton;
    private EditLinkedInFragment instance = null;
    private SetUserData updateLinkedIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        instance = this;
        View view = inflater.inflate(R.layout.fragment_edit_linked_in, container, false);
        editLinkedIn = (EditText) view.findViewById(R.id.editText);

        //@author: Hugh Leow
        //@brief:
        //API call to update the user's information with user input
        //See: SetUserData.java
        //@pre condition: New information not updated in database
        //@post condition: Request to update database with new information
        saveLinkedInButton = (Button) view.findViewById(R.id.saveLinkedIn);
        saveLinkedInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newLinkedIn = editLinkedIn.getText().toString();
                updateLinkedIn = new SetUserData(getContext(), instance);
                updateLinkedIn.setUserLinkedIn(newLinkedIn);
                saveLinkedInButton.setEnabled(false);
            }
        });


        return view;
    }

    //@author: Hugh Leow
    //@brief
    //Interface function for ASYNC HTTP request from SetUserData.java
    //If the database is updated successfully, close the fragment + activity, otherwise notify the user
    //@params: [Boolean success] [String message]
    //@pre condition: New information not updated in database
    //@post condition: Database updated with new information if success = 'true'
    @Override
    public void dataUpdateComplete(Boolean success, String message) {
        if (success) {
            getActivity().finish();
        }
        else {
            // show error message to user
            Toast t = Toast.makeText(getContext(), "ERROR. TRY AGAIN.", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
            saveLinkedInButton.setEnabled(true);
        }
    }
}