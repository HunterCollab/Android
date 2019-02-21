package com.example.socialmediaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegisterActivity extends AppCompatActivity {

    private EditText UserEmail;
    private EditText Password;
    private EditText ConfirmPassword;
    private  Button RegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserEmail = (EditText) findViewById(R.id.register_email);
        Password = (EditText) findViewById(R.id.register_password);
        ConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        RegisterButton = (Button) findViewById(R.id.register_create_account);
        MyLoopjTask myLoopTask = new MyLoopjTask(this, (MyLoopjTask.OnLoopComplete) this);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = UserEmail.getText().toString();
                String password = Password.getText().toString();
                UserEmail.setText("");
                Password.setText("");
               // myLoopTask.executeLoopjCall(email,password);

            }
        });




    }

}
