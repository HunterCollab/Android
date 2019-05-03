package com.example.socialmediaapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.socialmediaapp.activity.LoginActivity;


public class MainActivity extends AppCompatActivity {

    //Variable Declarations
    //Connects XML view to this logic file.
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private View navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Links variable mToolbar to the section in the activity_main.xml file with id main_app_toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_app_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        //Links variable drawerLayout to the section in the activity_main.xml file with id drawable_layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout);

        //Links variable recyclerView to the section in the activity_main.xml file with id all_user_post_list
        recyclerView = (RecyclerView) findViewById(R.id.all_users_post_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //Links variable navigationView to the section in the activity_main.xml file with id navigation_view
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
            navView = navigationView.inflateHeaderView(R.layout.navigation_header);


        layoutManager = new LinearLayoutManager(this);


        //Creates the hamburger button in the top tool bar next to home
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    //Will run automatically at the start of the app
    @Override
    protected void onStart() {
        super.onStart();
        SendUserToLoginActivity();
    }

    private void SendUserToLoginActivity() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
