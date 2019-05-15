package com.huntercollab.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialmediaapp.R;

import java.util.List;

public class ViewMembersAdapter extends RecyclerView.Adapter<ViewMembersAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    //@author: Hugh Leow
    //@brief: Data is passed into the constructor for the adapter
    //@params: [Context context] [List<String> data]
    public ViewMembersAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    //@author: Hugh Leow
    //@brief: Inflates the row layout from view_members_item.xml when needed
    //@params: [ViewGroup parents] [int viewType]
    //@pre condition: Rows not inflated inside the view
    //@post condition: Rows inflated inside the view
    //@return: ViewHolder with inflated views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_members_item, parent, false);
        return new ViewHolder(view);
    }

    //@author: Hugh Leow
    //@brief: Binds the data to TextView for each row
    //@pre condition: Items in the view not binded to the view
    //@post condition: Items binded to the view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String names = mData.get(position);
        holder.myTextView.setText(names);
    }

    //@author: Hugh Leow
    //@brief: Returns total number of rows
    //@return: int of total number of items for the view
    @Override
    public int getItemCount() {
        return mData.size();
    }


    //@author: Hugh Leow
    //@brief: Stores and recycles views as they are scrolled off the screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.memberName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem (int id) {
        return mData.get(id);
    }

    //@author: Hugh Leow
    //@brief: Allows click events by the user to be caught
    //@params: [ItemClickListener itemClickListener]
    //@pre condition: User has not clicked anything
    //@post condition: When user clicks something in the view, it is registered and action is taken
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
