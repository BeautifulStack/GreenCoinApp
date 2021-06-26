package com.example.fairrepack.utils;

import java.util.Date;

public class Tx {
    private int amount;
    private String receiver;
    private String sender;
    private long time;

    public Tx(int amount, String receiver, String sender, long time){
        this.amount = amount;
        this.receiver = receiver;
        this.sender = sender;
        this.time = time;
    }

    public int getAmount() {
        return amount;
    }

    public long getTime() {
        return time;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
