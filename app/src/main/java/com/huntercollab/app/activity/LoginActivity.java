package com.huntercollab.app.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.huntercollab.app.network.loopjtasks.DoLogin;

public class LoginActivity extends AppCompatActivity implements DoLogin.OnDoLoginComplete {

    private Button loginButton;
    private EditText userEmail;
    private EditText userPassword;
    private TextView needNewAccountLink;
    private LoginActivity instance = null;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // super calls on public/protected function from extends <super class>
        super.onCreate(savedInstanceState);
        instance = this;

        //connect XML file to this activity
        setContentView(R.layout.activity_login);

        // matching the variables to their views in res dir
        loginButton = (Button) findViewById(R.id.login_button);
        userEmail = (EditText) findViewById(R.id.login_email);
        userPassword = (EditText) findViewById(R.id.login_password);
        needNewAccountLink =  (TextView) findViewById(R.id.register_link);

        //@author: Hugh Leow & Edwin Quintuna
        //@brief: Sends user to RegisterActivity.java to create a new account
        //@pre condition: User is on login screen
        //@post condition: User is on register screen
        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });

        //@author: Hugh Leow & Edwin Quintuna
        //@brief:
        //Takes the users input values (username + password) and makes an API call to the server for login
        //See: DoLogin.java
        //@pre condition: User is on login screen
        //@post condition: Request sent to login, moves user to home screen if successful
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Variables from xml
                loginButton.setEnabled(false);
                needNewAccountLink.setEnabled(false);
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();
                DoLogin loginTask = new DoLogin(getApplicationContext(), instance);
                loginTask.doLogin(email, password);
            }
        });
    }

    // sends user to register screen
    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    // send user to collab screen (main screen)
    private void sendUserToCollabActivity() {
        Intent collabIntent = new Intent( LoginActivity.this, CollabListActivity.class);
        startActivity(collabIntent);
    }

    //@author: Hugh Leow & Edwin Quintuna
    //@brief:
    //Interface function from DoLogin.java
    //If server notifies us of success, user is logged in and sent to the 'home' screen
    //If login fails, user will see error message given to us by the server
    //@params: [Boolean success] [String message]
    //@pre condition: Request not sent for user to login
    //@post condition: If success = 'true', user is logged in and sent to the home screen, otherwise they will receive toast with error 'message'
    @Override
    public void loginCompleted(Boolean success, String message) {
        Log.i("received", "Success " + message);
        Log.i("listener implementation", "Listener implementation of loginCompleted working.");
        if (success) {
            sendUserToCollabActivity();
            finish();
        }
        else {
            // show user toast on fail
            Context context = LoginActivity.this;
            Toast t = Toast.makeText(context, message, Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
            loginButton.setEnabled(true);
            needNewAccountLink.setEnabled(true);
        }
    }
}
