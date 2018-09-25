package com.sapicons.deepak.k2psapicons.Others;

public class Messages {
    private String message;
    private long time;
    private boolean seen;
    private String from;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Messages() {
    }

    public Messages(String message, long time, boolean seen, String from) {
        this.message = message;
        this.seen = seen;
        this.time = time;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }


}
