package com.example.kankan.timepass;

public class Comment {
    private String timeLineid,profileId,comment,profileName,profileImage;

    public Comment( String timeLineid,  String profileId, String comment,String profileName,String profileImage) {

        this.timeLineid = timeLineid;
        this.profileId = profileId;
        this.comment = comment;
        this.profileName=profileName;
        this.profileImage=profileImage;
    }

    public Comment() {
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTimeLineid() {
        return timeLineid;
    }

    public void setTimeLineid(String timeLineid) {
        this.timeLineid = timeLineid;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
