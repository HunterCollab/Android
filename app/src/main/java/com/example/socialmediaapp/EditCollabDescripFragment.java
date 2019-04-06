package com.example.socialmediaapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialmediaapp.loopjtasks.UpdateCollabData;


public class EditCollabDescripFragment extends Fragment implements UpdateCollabData.UpdateCollabComplete {

    public EditCollabDescripFragment() {
        // Required empty public constructor
    }

    private EditText editDescrip;
    private Button saveDescripButton;
    private EditCollabDescripFragment instance = null;
    private UpdateCollabData updateDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        instance = this;
        View view = inflater.inflate(R.layout.fragment_edit_collab_description, container, false);
        editDescrip = (EditText) view.findViewById(R.id.editText);
        saveDescripButton = (Button) view.findViewById(R.id.saveDescrip);
        saveDescripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDescrip = editDescrip.getText().toString();
                updateDescription = new UpdateCollabData(getContext(), instance);
                updateDescription.updateCollabDescription(newDescrip);
                saveDescripButton.setEnabled(false);
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
            saveDescripButton.setEnabled(true);
        }
    }
}
