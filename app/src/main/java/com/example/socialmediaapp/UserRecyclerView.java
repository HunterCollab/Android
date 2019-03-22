package com.example.socialmediaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class UserRecyclerView extends RecyclerView.Adapter<UserRecyclerView.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> data_names = new ArrayList<>();

    private Context mContext;

    public UserRecyclerView(ArrayList<String> data_names, Context mContext) {
        this.data_names = data_names;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.
                from(viewGroup.getContext()).inflate(R.layout.user_data_layout, viewGroup, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");
        viewHolder.className.setText(data_names.get(i));
    }

    public void removeItem(int position){
        data_names.remove(position);
    }

    @Override
    public int getItemCount() {
        if (data_names != null){
            return data_names.size();
        } else {
            return 0;
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView className;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.data_name);
            parentLayout = itemView.findViewById(R.id.recycler_parent);
        }
    }
}
