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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

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



                AsyncHttpClient client = new AsyncHttpClient();
                client.get("https://huntercollabapi.herokuapp.com/user/createUser?username=newuser@myhunter.cuny.edu&password=Password123", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String testV = null;
                        JSONObject parsed = null;
                        try {
                            testV = new JSONObject(new String(responseBody)).toString();
                            parsed = new JSONObject(new String(responseBody));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("String: " + testV);
                        System.out.println("JSONobject" + parsed);

                        sendUserToRegisterActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = UserEmail.getText().toString();
                String password = UserPassword.getText().toString();
            }
        });




    }


    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);

    }
/*
        public static void call_me() throws Exception {
            String url = " https://huntercollabapi.herokuapp.com/user/createUser?username=newuser@myhunter.cuny.edu?password=whasd";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //print in String
            System.out.println(response.toString());
            //Read JSON response and print
            JSONObject myResponse = new JSONObject(response.toString());
            System.out.println("result after Reading JSON Response");
            System.out.println("statusCode- "+myResponse.getBoolean("success"));
            System.out.println("statusCode- " + myResponse.getString("error"));
        }
*/
}
