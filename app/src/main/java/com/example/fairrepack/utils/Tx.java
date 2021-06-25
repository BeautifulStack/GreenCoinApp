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
}
