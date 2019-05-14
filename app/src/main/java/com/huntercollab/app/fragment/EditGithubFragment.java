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


public class EditGithubFragment extends Fragment implements SetUserData.UpdateComplete {

    public EditGithubFragment() {
        // Required empty public constructor
    }

    private EditText editGithub;
    private Button saveGithubButton;
    private EditGithubFragment instance = null;
    private SetUserData updateGithub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        instance = this;
        View view = inflater.inflate(R.layout.fragment_edit_github, container, false);
        editGithub = (EditText) view.findViewById(R.id.editText);

        // API call to update the user's information with user input
        // See: SetUserData.java
        saveGithubButton = (Button) view.findViewById(R.id.saveGithub);
        saveGithubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newGithub = editGithub.getText().toString();
                updateGithub = new SetUserData(getContext(), instance);
                updateGithub.setUserGithub(newGithub);
                saveGithubButton.setEnabled(false);
            }
        });


        return view;
    }

    // Interface function for ASYNC HTTP request from SetUserData.java
    // If the database is updated successfully, close the fragment + activity, otherwise notify the user
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
            saveGithubButton.setEnabled(true);
        }
    }
}

