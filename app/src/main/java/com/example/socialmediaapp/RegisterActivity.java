package com.example.socialmediaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.loopjtasks.DoRegister;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class RegisterActivity extends AppCompatActivity {

    private EditText UserEmail;
    private EditText Password;
    private EditText confirmPassword;
    private  Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserEmail = (EditText) findViewById(R.id.register_email);
        Password = (EditText) findViewById(R.id.register_password);
        confirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        registerButton = (Button) findViewById(R.id.register_create_account);
        //MyLoopjTask myLoopTask = new MyLoopjTask(this, (MyLoopjTask.OnLoopComplete) this); //Not required -- At least for this page...

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = UserEmail.getText().toString();
                        String password = Password.getText().toString();
                        System.out.println(email);
                        DoRegister loginTask = new DoRegister();
                        loginTask.doRegister(email, password);

            }
        });
        


    }

}
