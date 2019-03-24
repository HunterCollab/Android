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
        saveLinkedInButton = (Button) view.findViewById(R.id.saveLinkedIn);
        saveLinkedInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newLinkedIn = editLinkedIn.getText().toString();
                updateLinkedIn = new SetUserData(getContext(), instance);
                updateLinkedIn.setUserLinkedIn(newLinkedIn);
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