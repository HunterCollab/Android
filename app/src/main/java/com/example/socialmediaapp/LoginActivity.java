package com.example.socialmediaapp;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.loopjtasks.DoLogin;

public class LoginActivity extends AppCompatActivity implements DoLogin.OnDoLoginComplete {

    private Button loginButton;
    private EditText userEmail;
    private EditText userPassword;
    private TextView needNewAccountLink;
    private LoginActivity instance = null;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_login);


        loginButton = (Button) findViewById(R.id.login_button);
        userEmail = (EditText) findViewById(R.id.login_email);
        userPassword = (EditText) findViewById(R.id.login_password);
        needNewAccountLink =  (TextView) findViewById(R.id.register_link);
        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Variables from xml
                // if user clicks button again in less than 3 seconds, it will not work
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000)
                    return;
                mLastClickTime = SystemClock.elapsedRealtime();
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();
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
            //Sent to main screen
        } else {
            Context context = LoginActivity.this;
            String login_error_message = "INVALID LOGIN, PLEASE TRY AGAIN";
            Toast t = Toast.makeText(context, login_error_message, Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
        }
    }
}
