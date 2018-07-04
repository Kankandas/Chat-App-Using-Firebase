package com.example.kankan.timepass;

public class Profile {

    private String name,relation,work,school,college,email,url,isOnline,userID,DeviceToken;


    public Profile() {
    }

    public Profile(String name, String relation, String work, String school, String college, String email, String url,String isOnline,
                   String userID,String DeviceToken) {
        this.name = name;
        this.relation = relation;
        this.work = work;
        this.school = school;
        this.college = college;
        this.email = email;
        this.url = url;
        this.isOnline=isOnline;
        this.userID=userID;
        this.DeviceToken=DeviceToken;

    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
