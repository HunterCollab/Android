package com.huntercollab.app.loopjtasks;

public class MessageModel {
    private String sender;
    private String displayName;
    private String message;
    private long time;

    public MessageModel (String sender, String message, long time, String displayName){
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.displayName = displayName;
    }

    public String getSender(){ return sender; }

    public String getDisplayName(){ return displayName; }

    public String getMessage() { return message; }

    public long getTime(){ return time; }
}