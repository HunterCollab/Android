package com.example.socialmediaapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialmediaapp.loopjtasks.SetUserData;


public class EditNameFragment extends Fragment implements SetUserData.UpdateComplete {

    public EditNameFragment() {
        // Required empty public constructor
    }

    private EditText editName;
    private Button saveNameButton;
    private EditNameFragment instance = null;
    private SetUserData updateName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        instance = this;
        View view = inflater.inflate(R.layout.fragment_edit_name, container, false);
        editName = (EditText) view.findViewById(R.id.editText);
        saveNameButton = (Button) view.findViewById(R.id.saveName);
        saveNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editName.getText().toString();
                updateName = new SetUserData(getContext(), instance);
                updateName.setUserNickname(newName);
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
