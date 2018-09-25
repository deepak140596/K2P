package com.sapicons.deepak.k2psapicons.Others;

public class Chat {
   public String title;
    public String price;
    public String chatUser;
    public boolean seen;
    public long timestamp;
    public String ads;
    public String chatUserId;
    public String image;

    public Chat(){}

    public Chat(String title, String price, String chatUser, boolean seen, long timestamp, String ads, String chatUserId, String image) {
        this.title = title;
        this.price = price;
        this.chatUser = chatUser;
        this.seen = seen;
        this.timestamp = timestamp;
        this.ads = ads;
        this.chatUserId = chatUserId;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChatUser() {
        return chatUser;
    }

    public void setChatUser(String chatUser) {
        this.chatUser = chatUser;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    public String getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(String chatUserId) {
        this.chatUserId = chatUserId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
