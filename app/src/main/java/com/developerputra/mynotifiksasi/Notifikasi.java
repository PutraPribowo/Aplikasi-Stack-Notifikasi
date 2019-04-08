package com.developerputra.mynotifiksasi;

public class Notifikasi {
    private int id;
    private String sender;
    private String message;

    Notifikasi(int id, String sender, String message) {
        this.id = id;
        this.sender = sender;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

