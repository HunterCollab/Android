package com.example.socialmediaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialmediaapp.config.GlobalConfig;
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
        //MyLoopjTask myLoopTask = new MyLoopjTask(this, (MyLoopjTask.OnLoopComplete) this); //Not required -- At least for this page...

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = UserEmail.getText().toString();
                String password = Password.getText().toString();
                System.out.println(email);
                AsyncHttpClient httpClient = GeneralTools.createAsyncHttpClient(getApplicationContext());
                RequestParams params = new RequestParams();
                params.put("username", email);
                params.put("password", password);
                httpClient.get(GlobalConfig.BASE_API_URL + "/user/createUser", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.has("success") && response.getBoolean("success")) { //Success variable is true.
                                System.out.println(response.toString());
                                // TODO: Tell user account was successfully created
                                // TODO: Send user to login page so they can login with the account they just created.
                            } else {
                                String error = response.getString("error"); //Extract the error
                                System.out.println("Error: " + error);
                                // TODO: Push error to screen
                            }
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        // TODO: Push error to screen
                    }
                });
            }
        });




    }

}
