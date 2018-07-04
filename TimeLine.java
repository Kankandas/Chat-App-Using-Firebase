package com.example.kankan.timepass;

public class TimeLine {
    private String title;
    private String uri;
    private String id,text,profileName,profileImageUri,typeOfTimeline,noOFlikes,timeLineid;

    public TimeLine() {
    }

    public TimeLine(String title, String uri,String id,String text,String profileName,String profileImageUri
                    ,String typeOfTimeline,String timeLineid,String noOFlikes) {
        this.title = title;
        this.uri = uri;
        this.id =id;
        this.text=text;
        this.profileImageUri=profileImageUri;
        this.profileName=profileName;
        this.typeOfTimeline=typeOfTimeline;
        this.timeLineid=timeLineid;
        this.noOFlikes=noOFlikes;
    }

    public String getNoOFlikes() {
        return noOFlikes;
    }

    public void setNoOFlikes(String noOFlikes) {
        this.noOFlikes = noOFlikes;
    }

    public String getTimeLineid() {
        return timeLineid;
    }

    public void setTimeLineid(String timeLineid) {
        this.timeLineid = timeLineid;
    }

    public String getTypeOfTimeline() {
        return typeOfTimeline;
    }

    public void setTypeOfTimeline(String typeOfTimeline) {
        this.typeOfTimeline = typeOfTimeline;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
