package com.example.fairrepack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.service.quickaccesswallet.WalletCard;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private CardView bills;
    private CardView send;
    private CardView wallet;
    private ImageView Gwallet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bills = (CardView) findViewById(R.id.bills);
        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        send = (CardView) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });


        wallet = (CardView) findViewById(R.id.wallet);
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity5();
            }
        });

        Gwallet = (ImageView) findViewById(R.id.newwallet);
        Gwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity4();
            }
        });


    }

    public void openActivity2() {
        Intent intent = new Intent(this, Bills.class);
        startActivity(intent);
    }

    public void openActivity3(){
        Intent intent = new Intent(this, Send.class);
        startActivity(intent);
    }

    public void openActivity5(){
        Intent intent = new Intent(this, Wallet.class);
        startActivity(intent);
    }



    public void openActivity4(){
        Intent intent = new Intent(this, GenerateWallet.class);
        startActivity(intent);
    }


}