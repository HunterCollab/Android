package com.example.socialmediaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> skillNames;
    private Context mContext = null;


    public RecyclerViewAdapter(ArrayList<String> skills, Context mContext) {
        this.mContext = mContext;
        this.skillNames = new ArrayList<>();
        update(skills);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_skills_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        viewHolder.userSkill.setText(skillNames.get(i));

        viewHolder.skillsParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
            }
        });

    }

    @Override
    public int getItemCount() {


            return skillNames.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView userSkill;
        Button removeSkill;
        RelativeLayout skillsParentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userSkill = itemView.findViewById(R.id.user_skill);
            removeSkill = itemView.findViewById(R.id.remove_skill_button);
            skillsParentLayout = itemView.findViewById(R.id.skills_parent_layout);
        }
    }

    public void update(ArrayList<String> data) {

            skillNames.clear();
            skillNames.addAll(data);
            notifyDataSetChanged();

    }
}
