package com.example.kankan.timepass;

public class Message {
    private String currentUser;
    private String message;
    private String senderUser;
    private String time;

    public Message() {
    }

    public Message(String currentUser, String message, String senderUser,String time) {

        this.currentUser = currentUser;
        this.message = message;
        this.senderUser = senderUser;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(String senderUser) {
        this.senderUser = senderUser;
    }
}
