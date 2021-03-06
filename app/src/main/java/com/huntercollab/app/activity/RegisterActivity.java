package com.huntercollab.app.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.huntercollab.app.network.loopjtasks.DoRegister;


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

        //@author: Hugh Leow & Edwin Quintuna
        //@brief:
        //Takes user input for username + password + confirm password
        //API call with user input to attempt registration
        //Checks if passwords match before attempting API call
        //@pre condition: User is not registered
        //@post condition: Request for user to register new account
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

    //@author: Hugh Leow & Edwin Quintuna
    //@brief:
    //Interface function for ASYNC HTTP request from DoRegister.java
    //If user successfully registers a new account, they are sent to the 'home' screen
    //If user fails to register, user will be notified of 'message' from the server
    //@params: [Boolean success] [String message]
    //@pre condition: User is not registered
    //@post condition: User is registered if success = 'true', or if success = 'false' then toast will be shown with error 'message'
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
