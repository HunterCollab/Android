package com.example.socialmediaapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialmediaapp.loopjtasks.SetUserData;


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
        saveGithubButton = (Button) view.findViewById(R.id.saveGithub);
        saveGithubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newGithub = editGithub.getText().toString();
                updateGithub = new SetUserData(getContext(), instance);
                updateGithub.setUserGithub(newGithub);
                getActivity().finish();
            }
        });


        return view;
    }

    @Override
    public void dataUpdateComplete(Boolean success, String message) {
        System.out.println(message);
    }
}

