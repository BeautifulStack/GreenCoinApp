package com.example.fairrepack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.fairrepack.utils.Wallet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    String address = null;
    private CardView bills;
    private CardView send;
    private CardView wallet;
    private ImageView Gwallet;
    private TextView balance;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        balance = findViewById(R.id.balance);
        imageView2 = findViewById(R.id.imageView2);

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

        get_balance();

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_balance();
            }
        });


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
                openActivity5(address);
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

    public void openActivity5(String address) {
        Intent intent = new Intent(this, WalletActivity.class);
        intent.putExtra("address", address);
        startActivity(intent);
    }

    public void get_balance() {
        Gson gson = new GsonBuilder().create();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://blockchain.octobyte.cloud/balance/" + address)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Couldn't retrieve wallet balance", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Response response_json = gson.fromJson(myResponse, Response.class);
                            balance.setText(response_json.balance + " Coins");
                        }
                    });
                } else {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Couldn't retrieve wallet balance", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }


    public void openActivity4() {
        Intent intent = new Intent(this, GenerateWallet.class);
        startActivity(intent);
    }

    static class Response {
        int balance;
        int new_balance;
    }


}