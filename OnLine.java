package com.example.kankan.timepass;

public class OnLine {

    private String isOnline,userId;

    public OnLine() {
    }

    public OnLine(String isOnline, String userId) {
        this.isOnline = isOnline;
        this.userId = userId;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
