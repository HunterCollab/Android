package com.huntercollab.app.utils;

public class Interfaces {
    public interface DownloadComplete {
        public void downloadComplete(Boolean success);
    }

    public interface DownloadProfleComplete {
        public void downloadProfileComplete(Boolean success);
    }

    public interface OwnerDownloadComplete {
        public void ownerDownloadComplete(Boolean success);
    }
}
