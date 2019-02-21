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
import com.example.socialmediaapp.tools.GeneralTools;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private EditText UserEmail;
    private EditText UserPassword;
    private TextView NeedNewAccountLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                String email = UserEmail.getText().toString();
                String password = UserPassword.getText().toString();
                AsyncHttpClient httpClient = GeneralTools.createAsyncHttpClient(getApplicationContext());
                RequestParams params = new RequestParams();
                params.put("username", email);
                params.put("password", password);
                httpClient.get(GlobalConfig.BASE_API_URL + "/auth/login", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.has("success") && response.getBoolean("success")) { //Success variable is true.
                                String token = response.getString("token"); //Extract the token
                                // Retrieve cookie store for this application context.
                                PersistentCookieStore myCookieStore = new PersistentCookieStore(getApplicationContext());
                                // Create & save cookie into the cookie store.
                                BasicClientCookie newCookie = new BasicClientCookie("capstoneAuth", token);
                                newCookie.setDomain("huntercollabapi.herokuapp.com");
                                newCookie.setPath("/");
                                myCookieStore.addCookie(newCookie);
                                System.out.println("Token successfully retrieved and saved to cookie store: " + token);
                                //TODO: Send user to main page.
                            } else {
                                String error = response.getString("error"); //Extract the error
                                System.out.println("Error: " + error);
                                //TODO: Push error to screen
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

    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
