package com.example.socialmediaapp.loopjtasks;

public class MessageModel {
    private String sender;
    private String message;
    private long time;

    public MessageModel (String sender, String message, long time){
        this.sender = sender;
        this.message = message;
        this.time = time;
    }

    public String getSender(){ return sender; }

    public String getMessage() { return message; }

    public long getTime(){ return time; }
}