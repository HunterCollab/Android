package com.example.socialmediaapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> data;
    //private final Context context;

    //Constructor that initializes an instance of the Class with the values of Context and resources being passed down.
    public AutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        //this.context = context;
        this.data = new ArrayList<>();
    }

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


}
