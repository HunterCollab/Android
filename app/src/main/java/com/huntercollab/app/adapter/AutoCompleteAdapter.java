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

    //@author: Edwin Quintuna
    //@brief:
    //Constructor that initializes an instance of the Class with the values of Context and resources being passed
    //@params: [Context context] [int resource]
    public AutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        //this.context = context;
        this.data = new ArrayList<>();
    }

    //@author: Edwin Quintuna
    //@brief: Filter results received
    //@pre condition: Results retrieved not filtered to the user's input
    //@post condition: Results filtered based on user's input
    //@return: Filtered results
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


    //@author: Edwin Quintuna
    //@brief: Set data with filtered results
    //@params: [List<String> list]
    public void setData(List<String> list) {
        data.clear();
        data.addAll(list);
    }

    //Key word @Override will override tha method from its superclass with the same name.
    //@author: Edwin Quintuna
    //@brief: Returns the # of items in the data set
    //@return: Int number of items in the dataset
    @Override
    public int getCount()
    {
        return data.size();
    }

    //@Nullable will allow a method that should return a string to be able to return a string
    //@author: Edwin Quintuna
    //@brief: Returns the item wanted when user clicks on it
    //@return: String inside the list that the user clicks into the text box
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
