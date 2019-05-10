package com.huntercollab.app.network.loopjtasks.realtime;

import android.os.AsyncTask;
import android.os.Message;

import com.huntercollab.app.activity.MessagingActivity;
import com.huntercollab.app.config.GlobalConfig;
import com.huntercollab.app.utils.GeneralTools;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RealtimeAsync extends AsyncTask<MessagingActivity, Void, Void> {

    private Socket socket;
    private DataOutputStream dout;
    private DataInputStream din;
    private boolean stop = false;

    @Override
    protected Void doInBackground(MessagingActivity... param) {
        try {
            MessagingActivity messagingActivity = param[0];
            String token = GeneralTools.getAuthToken(messagingActivity.getApplicationContext());
            if (token == null) {
                System.out.println("Auth token not found.");
                return null;
            }
            //System.out.println(token);

            socket = new Socket(GlobalConfig.HOST, GlobalConfig.RMS_PORT);
            dout = new DataOutputStream(socket.getOutputStream());
            din = new DataInputStream(socket.getInputStream());

            //Authorize the client
            this.writeMessage(token); //First Message is the Auth Token.

            //Make sure OK response is received.
            String response = this.getNextMessage();
            if (!response.equals("AUTH_SUCCESS")) {
                System.out.println("RMS Auth failed.");
                return null;
            }

            while (!stop) {
                String msg = this.getNextMessage();
                if (msg.equals("PING")) {
                    System.out.println("Received ping. Checking for new message.");
                    messagingActivity.getHandlerThread().sendMessage(new Message());
                    //messagingActivity.downloadComplete(true);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return null;
    }

    public String getNextMessage() throws IOException {
        return RMSProtocol.readUTF(this.din);
    }

    public void writeMessage(String msg) throws IOException {
        RMSProtocol.writeUTF(msg, dout); //First Message is the Token.
    }

    public void killConn() {
        this.stop = true;
        if (this.socket != null) {
            try {
                this.socket.close();
                this.socket = null;
            } catch (IOException ioe) {
                //Nothing....
            }
        }
    }

    @Override
    protected void onPostExecute(Void value) { }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}
