package com.example.fairrepack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.fairrepack.utils.Wallet;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    String address = null;
    private CardView bills;
    private CardView send;
    private CardView wallet;
    private ImageView Gwallet;
    private Button chooseAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        File file = new File(context.getFilesDir(), "private.key");
        if (!file.exists()) {
            Intent i = new Intent(MainActivity.this, GenerateWallet.class);
            startActivity(i);
            finish();
        } else {
            Wallet wallet = Wallet.get_wallet(context.getFilesDir().getPath(), context);
            if (wallet == null) {
                Toast.makeText(MainActivity.this, "Corrupted key, clear storage to generate a new one", Toast.LENGTH_LONG).show();
            } else {
                System.out.println("DEBUG : public key : " + wallet.getAddress());
                address = wallet.getAddress();
            }
        }

        bills = (CardView) findViewById(R.id.bills);
        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        chooseAmount = (Button) findViewById(R.id.chooseAmount);
        chooseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferAmount();
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
        Intent intent = new Intent(this, BillsActivity.class);
        startActivity(intent);
    }

    public void openActivity3() {
        Intent intent = new Intent(this, SendActivity.class);
        startActivity(intent);
    }

    public void openActivity5() {
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }


    public void openActivity4() {
        Intent intent = new Intent(this, GenerateWallet.class);
        startActivity(intent);
    }

    public void TransferAmount() {
        Intent intent = new Intent(this, ChooseAmount.class);
        startActivity(intent);
    }


}