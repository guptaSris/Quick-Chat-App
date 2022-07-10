package com.example.quickchatapp;

public class Messages {

String message,sendId;
long timeStamp;

    public Messages() {
    }

    public Messages(String message, String sendId, long timeStamp) {
        this.message = message;
        this.sendId = sendId;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
