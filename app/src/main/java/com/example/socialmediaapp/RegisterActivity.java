package com.example.socialmediaapp;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialmediaapp.config.GlobalConfig;
import com.example.socialmediaapp.loopjtasks.DoLogin;
import com.example.socialmediaapp.loopjtasks.DoRegister;
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class RegisterActivity extends AppCompatActivity implements DoRegister.OnDoRegisterComplete {

    private EditText UserEmail;
    private EditText Password;
    private EditText confirmPassword;
    private Button registerButton;
    private RegisterActivity instance = null;
    private long mLastClickTime = 0;
    private Context context = RegisterActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_register);

        UserEmail = (EditText) findViewById(R.id.register_email);
        Password = (EditText) findViewById(R.id.register_password);
        confirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        registerButton = (Button) findViewById(R.id.register_create_account);
        //MyLoopjTask myLoopTask = new MyLoopjTask(this, (MyLoopjTask.OnLoopComplete) this); //Not required -- At least for this page...

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prevents user from clicking button multiple times in less than 3 seconds
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000)
                    return;
                mLastClickTime = SystemClock.elapsedRealtime();
                String email = UserEmail.getText().toString();
                String password = Password.getText().toString();
                String confirmPass = confirmPassword.getText().toString();
                // System.out.println(email);
                DoRegister loginTask = new DoRegister(getApplicationContext(), instance);
                if (password.equals(confirmPass) && password.length() > 5) {
                    loginTask.doRegister(email, password);
                }
                else {
                    String message = "PASSWORDS MUST BE 6+ CHARACTERS AND MATCH";
                    Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }
            }
        });
    }

    @Override
    public void registerCompleted(Boolean success, String message) {
        System.out.println(success + ": " + message);
        System.out.println("Listener implementation of registerCompleted working.");
        if (success) {
            // TODO: Send to user details screen
        } else {
            // TODO: FIND A WAY TO PUSH ERROR MESSAGES FROM API TO USER
            String register_error_message = "EMAIL MUST BE MYHUNTER.CUNY.EDU";
            Toast t = Toast.makeText(context, register_error_message, Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
        }
    }

}
