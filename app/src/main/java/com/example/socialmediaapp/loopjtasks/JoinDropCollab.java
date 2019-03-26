package com.example.socialmediaapp.loopjtasks;

import android.content.Context;

public class JoinDropCollab {

    private Context context;
    private JoinDropComplete listener;

    public JoinDropCollab(Context context, JoinDropComplete listener){
        this.context = context;
        this.listener = listener;
    }

    public void joinCollab(String userId){
        System.out.println("userId: " + userId);
    }

    public interface JoinDropComplete {

        public void joinDropComplete(Boolean success);
    }
}
