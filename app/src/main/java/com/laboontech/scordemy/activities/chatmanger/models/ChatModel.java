package com.laboontech.scordemy.activities.chatmanger.models;

public class ChatModel {


    private String videoId;

    private String sendername;

    private String senderImage;

    private String senderId;

    private String message;

    private String messageTime;

    public ChatModel() {
    }

    public ChatModel(String videoId,String senderId, String sendername, String senderImage, String message, String messageTime) {
        this.videoId = videoId;
        this.sendername = sendername;
        this.senderImage = senderImage;
        this.message = message;
        this.messageTime = messageTime;
        this.senderId=senderId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
