package com.example.socialmediaapp.loopjtasks;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;


public class CollabModel implements Parcelable {
    public int id;
    String owner;
    Integer size;
    JSONArray members;
    String date;
    String duration;
    String location;
    Boolean status;
    String title;
    String description;
    JSONArray classes;
    JSONArray skills;
    JSONArray applicants;

    CollabModel(
            int id,
            String owner,
            int size,
            JSONArray members,
            String duration,
            String date,
            String location,
            Boolean status,
            String title,
            String description,
            JSONArray classes,
            JSONArray skills,
            JSONArray applicants){

        this.id = id;
        this.owner = owner;
        this.size = size;
        this.members = members;
        this.duration = duration;
        this.date = date;
        this.location = location;
        this.status = status;
        this.title = title;
        this.description = description;
        this.classes = classes;
        this.skills = skills;
        this.applicants = applicants;
    }

    public CollabModel(Parcel in) {
        id = in.readInt();
        owner = in.readString();
        size = in.readInt();
        duration = in.readString();
        date = in.readString();
        location = in.readString();
        byte tmpStatus = in.readByte();
        status = tmpStatus == 0 ? null : tmpStatus == 1;
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<CollabModel> CREATOR = new Creator<CollabModel>() {
        @Override
        public CollabModel createFromParcel(Parcel in) {
            return new CollabModel(in);
        }

        @Override
        public CollabModel[] newArray(int size) {
            return new CollabModel[size];
        }
    };

    public String getOwner(){
        return owner;
    }
    public int getSize(){ return size; }
    public JSONArray getMembers(){
        return members;
    }
    public String getDate(){
        return date;
    }
    public String getDuration(){
        return duration;
    }
    public String getLocation(){
        return location;
    }
    public Boolean getStatus(){
        return status;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public JSONArray getClasses(){
        return classes;
    }
    public JSONArray getSkills(){
        return skills;
    }
    public JSONArray getApplicants(){
        return applicants;
    }
    public int getId() {return id;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(owner);
        dest.writeInt(size);
        dest.writeString(duration);
        dest.writeString(date);
        dest.writeString(location);
        dest.writeByte((byte) (status == null ? 0 : status ? 1 : 2));
        dest.writeString(title);
        dest.writeString(description);
    }
}
