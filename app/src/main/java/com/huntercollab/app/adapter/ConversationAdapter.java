package com.huntercollab.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialmediaapp.R;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    //@author: Hugh Leow
    //@brief: Data is passed into the constructor in order to apply functions
    //@params: [Context context] [List<String> data]
    public ConversationAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    //@author: Hugh Leow
    //@brief: Inflates the row layout from view_messages_item.xml when needed
    //@params: [ViewGroup parent] [int viewType]
    //@pre condition: row not inflated for the message item
    //@post condition: row inflated for the message item
    //@return: ViewHolder with new inflated row layout added
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_messages_item, parent, false);
        return new ViewHolder(view);
    }

    //@author: Hugh Leow
    //@brief: Binds data to TextView for each row
    //@params: [ViewHolder holder] [int position]
    //@pre condition: views are not binded to the view holder
    //@post condition: views are binded to the view holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String names = mData.get(position);
        holder.myTextView.setText(names);
    }

    //@author: Hugh Leow
    //@brief: Returns total number of items in the List
    //@return: int for total number of active conversations
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
            myTextView = itemView.findViewById(R.id.messageName);
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
