package com.huntercollab.app.utils;

public class Interfaces {

    //@author: Hugh Leow & Edwin Quintuna
    //@brief: Interface function to pass Boolean to GetUserData.java
    //@pre condition: No request sent and/or response not received
    //@post condition: Response received and values passed
    public interface DownloadComplete {
        public void downloadComplete(Boolean success);
    }

    //@author: Hugh Leow
    //@brief: Interface function to pass Boolean to GetUserData.java
    //@pre condition: No request sent and/or response not received
    //@post condition: Response received and values passed
    public interface DownloadProfleComplete {
        public void downloadProfileComplete(Boolean success);
    }

    //@author: Hugh Leow
    //@brief: Interface function to pass Boolean to GetUserData.java
    //@pre condition: No request sent and/or response not received
    //@post condition: Response received and values passed
    public interface OwnerDownloadComplete {
        public void ownerDownloadComplete(Boolean success);
    }
}
