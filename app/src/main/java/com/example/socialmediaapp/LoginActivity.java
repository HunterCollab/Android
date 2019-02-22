package com.example.socialmediaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.loopjtasks.DoLogin;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class LoginActivity extends AppCompatActivity implements DoLogin.OnDoLoginComplete {

    private Button LoginButton;
    private EditText UserEmail;
    private EditText UserPassword;
    private TextView NeedNewAccountLink;
    private LoginActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_login);



        LoginButton = (Button) findViewById(R.id.login_button);
        UserEmail = (EditText) findViewById(R.id.login_email);
        UserPassword = (EditText) findViewById(R.id.login_password);
        NeedNewAccountLink =  (TextView) findViewById(R.id.register_link);
        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Variables from xml
                String email = UserEmail.getText().toString();
                String password = UserPassword.getText().toString();
                DoLogin loginTask = new DoLogin(getApplicationContext(), instance);
                loginTask.doLogin(email, password);
            }
        });
    }

    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    @Override
    public void loginCompleted(Boolean success, String message) {
        System.out.println(success + ": " + message);
        System.out.println("Listener implementation of loginCompleted working.");
        if (success) {
            //Sent to main
        } else {
            //Show user the message which is an error
        }
    }
}
