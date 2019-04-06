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


public class EditCollabTitleFragment extends Fragment implements UpdateCollabData.UpdateCollabComplete {

    public EditCollabTitleFragment() {
        // Required empty public constructor
    }

    private EditText editTitle;
    private Button saveTitleButton;
    private EditCollabTitleFragment instance = null;
    private UpdateCollabData updateTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        instance = this;
        View view = inflater.inflate(R.layout.fragment_edit_collab_title, container, false);
        editTitle = (EditText) view.findViewById(R.id.editText);
        saveTitleButton = (Button) view.findViewById(R.id.saveTitle);
        saveTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = editTitle.getText().toString();
                updateTitle = new UpdateCollabData(getContext(), instance);
                updateTitle.updateCollabTitle(newTitle);
                saveTitleButton.setEnabled(false);
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
            saveTitleButton.setEnabled(true);
        }
    }
}
