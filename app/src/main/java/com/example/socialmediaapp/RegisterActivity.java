package com.example.socialmediaapp;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        // super calls on function from the extends <class> (protected and public only)
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_register);

        UserEmail = (EditText) findViewById(R.id.register_email);
        Password = (EditText) findViewById(R.id.register_password);
        confirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        registerButton = (Button) findViewById(R.id.register_create_account);
        //MyLoopjTask myLoopTask = new MyLoopjTask(this, (MyLoopjTask.OnLoopComplete) this); //Not required -- At least for this page...

        // create account button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setEnabled(false);
                String email = UserEmail.getText().toString();
                String password = Password.getText().toString();
                String confirmPass = confirmPassword.getText().toString();
                DoRegister registerTask = new DoRegister(getApplicationContext(), instance);

                // passwords must match in order to send HTTP request
                if (password.equals(confirmPass)) {
                    registerTask.doRegister(email, password);
                }
                else {
                    String message = "Passwords must match.";
                    Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }
            }
        });
    }

    // send user to collab screen (main screen)
    private void sendUserToCollabScreen() {
        Intent collabIntent = new Intent( RegisterActivity.this, CollabListActivity.class);
        startActivity(collabIntent);
    }

    // abstract function from DoRegister.java defined here
    @Override
    public void registerCompleted(Boolean success, String message) {
        if (success) {
            sendUserToCollabScreen();
            finish();
        } else {
            // show server error message to user
            Toast t = Toast.makeText(context, message, Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
            registerButton.setEnabled(true);
        }
    }

}
