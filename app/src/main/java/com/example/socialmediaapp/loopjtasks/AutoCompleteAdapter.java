package com.example.socialmediaapp.loopjtasks;

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

    private Filter skillsFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            final FilterResults results = new FilterResults();


            if(constraint != null)
            { //If there is a constrain
                //From this point we connect to the database using HTTP requests to retrieve data
                AsyncHttpClient asyncHttpClient = GeneralTools.createAsyncHttpClient(getContext());
                final RequestParams requestParams = new RequestParams();
                String filterPattern = constraint.toString().toLowerCase().trim();
                requestParams.put("query", filterPattern);

                System.out.println("URL: " + GlobalConfig.BASE_API_URL + "/search/skills" + requestParams);
                asyncHttpClient.get(GlobalConfig.BASE_API_URL + "/search/skills", requestParams, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);


                        System.out.println("Response: " + response);
                        try {
                            JSONArray terms = response.getJSONArray("matches");
                            List<String> suggestions = new ArrayList<>();

                            System.out.println("here");
                            for(int i=0; i < terms.length(); i++){
                                String term = terms.getString(i);
                                System.out.println("term: " + term);
                                suggestions.add(term);
                            }
                            results.values = suggestions;
                            results.count = suggestions.size();
                            data = suggestions;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

            } else {

            }

            System.out.println("results: " + results.values);
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

                System.out.println("publishResults: " + results.values);
                notifyDataSetChanged();



        }
    };


    @Nullable
    @Override
    public Filter getFilter()
    {
        return skillsFilter;
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
