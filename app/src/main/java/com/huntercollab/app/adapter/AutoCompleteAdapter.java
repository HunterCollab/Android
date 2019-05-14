package com.huntercollab.app.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> data;
    //private final Context context;

    //Constructor that initializes an instance of the Class with the values of Context and resources being passed down.
    public AutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        //this.context = context;
        this.data = new ArrayList<>();
    }

    // Filter results received
    @Override
    public Filter getFilter() {
        Filter skillsFilter = new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                final FilterResults filterResults = new FilterResults();

                if(constraint != null)
                {
                filterResults.values = data;
                filterResults.count = data.size();

                }


                return filterResults;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if(results != null && (results.count > 0)){
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }


            }
        };

        return skillsFilter;
    }


    // Set data with filtered results
    public void setData(List<String> list) {
        data.clear();
        data.addAll(list);
    }

    //Key word @Override will override tha method from its superclass with the same name.
    //Will return the number of items in the array ArrayList<String>
    @Override
    public int getCount()
    {
        return data.size();
    }

    //@Nullable will allow a method that should return a string to be able to return a string
    //Uses the position of an item in the array to return the item.
    @Nullable
    @Override
    public String getItem(int position)
    {
        return data.get(position);
    }

    public void removeItem(int position){
        data.remove(position);
        notifyDataSetChanged();
    }

    public void restoreItem(String item, int position) {
        data.add(position, item);
        notifyDataSetChanged();
    }


}
