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

            //@author: Hugh Leow & Ram Vakada
            //@brief: Creates connection to the port on the backend server
            socket = new Socket(GlobalConfig.HOST, GlobalConfig.RMS_PORT);

            //@author: Hugh Leow & Ram Vakada
            //@brief: Output stream for the connected socket
            dout = new DataOutputStream(socket.getOutputStream());

            //@author: Hugh Leow & Ram Vakada
            //@brief: Input stream for the connected socket
            din = new DataInputStream(socket.getInputStream());

            //Authorize the client
            this.writeMessage(token); //First Message is the Auth Token.

            //@author: Hugh Leow & Ram Vakada
            //@brief: Response from server indicating 'real-time messaging' active or not
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

    //@author: Hugh Leow & Ram Vakada
    //@brief: Gets message from 'din' and returns the message
    //@return: String of next message
    public String getNextMessage() throws IOException {
        return RMSProtocol.readUTF(this.din);
    }

    //@author: Hugh Leow & Ram Vakada
    //@brief: Puts message into 'dout' and sends message to the server
    //@params: [String msg]
    public void writeMessage(String msg) throws IOException {
        RMSProtocol.writeUTF(msg, dout); //First Message is the Token.
    }

    //@author: Hugh Leow & Ram Vakada
    //@brief: Closes connection between the socket and the server
    //@pre condition: connection to server is open
    //@post condition: connection to server is closed
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
