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
import com.huntercollab.app.loopjtasks.SetUserData;


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
                saveNameButton.setEnabled(false);
            }
        });
        return view;
    }

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
            saveNameButton.setEnabled(true);
        }
    }
}
