package com.huntercollab.app.fragment;

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
import com.huntercollab.app.network.loopjtasks.UpdateCollabData;


public class EditCollabSizeFragment extends Fragment implements UpdateCollabData.UpdateCollabComplete {

    public EditCollabSizeFragment() {
        // Required empty public constructor
    }

    EditCollabTitleFragment.OnDataPass dataPasser;
    EditCollabSizeFragment.OnSizePass sizePasser;
    private String collabid;
    private EditText editSize;
    private Button saveSizeButton;
    private EditCollabSizeFragment instance = null;
    private UpdateCollabData updateSize;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (EditCollabTitleFragment.OnDataPass) context;
        sizePasser = (EditCollabSizeFragment.OnSizePass) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //@author: Hugh Leow
        //@brief: Used to update the correct collaboration
        collabid = dataPasser.onDataPass();

        // Inflate the layout for this fragment
        instance = this;
        View view = inflater.inflate(R.layout.fragment_edit_collab_size, container, false);
        editSize = (EditText) view.findViewById(R.id.editText);

        //@author: Hugh Leow
        //@brief:
        //Checks if new size is less than current number of members, if so notify user
        //API call to update the collaboration data from VALID user input
        //See: UpdateCollabData.java
        //@pre condition: New information not updated in database
        //@post condition: Request to update database with new information
        saveSizeButton = (Button) view.findViewById(R.id.saveSize);
        saveSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newSize = editSize.getText().toString();
                int sizeInt = Integer.parseInt(newSize);
                if (sizeInt < sizePasser.onSizePass()){
                    Toast t = Toast.makeText(getContext(), "Size cannot be less than # of current members.", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                } else {
                    updateSize = new UpdateCollabData(getContext(), instance);
                    updateSize.updateCollabSize(sizeInt, collabid);
                    saveSizeButton.setEnabled(false);
                }

            }
        });
        return view;
    }

    //@author: Hugh Leow
    //@brief:
    //Interface function for ASYNC HTTP request from UpdateCollabData.java
    //If the database is updated successfully, close the fragment + activity, otherwise notify the user
    //@params: [Boolean success]
    //@pre condition: New information not updated in database
    //@post condition: Database updated with new information if success = 'true'
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
            saveSizeButton.setEnabled(true);
        }
    }

    public interface OnSizePass{
        public int onSizePass();
    }
}
