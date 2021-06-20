package com.example.fairrepack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fairrepack.utils.Wallet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ValidateActivity extends AppCompatActivity {
    String address = null;
    int amount = 0;
    private TextView address_view, amount_view;
    private Button validate_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);

        address = getIntent().getStringExtra("address");
        amount = getIntent().getIntExtra("amount", 0);

        address_view = findViewById(R.id.address_tx);
        address_view.setText(address);

        amount_view = findViewById(R.id.amount_tx);
        amount_view.setText(amount+" GC");

        validate_btn = findViewById(R.id.validate_btn);

        validate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Wallet wallet = Wallet.get_wallet(context.getFilesDir().getPath(), context);

                if (wallet != null) {
                    send_transaction(wallet);
                }
            }
        });
    }

    private void send_transaction(Wallet wallet) {
        // JSON
        Gson gson = new GsonBuilder().create();

        Transaction transaction = new Transaction(amount, address, wallet.getAddress());
        String transaction_str = gson.toJson(transaction);

        RequestTx requestTx = new RequestTx(transaction, wallet.sign(transaction_str), wallet.getPublicKey());
        String json = gson.toJson(requestTx);

        //Toast.makeText(ValidateActivity.this, json, Toast.LENGTH_LONG).show();

        // HTTP
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://blockchain.octobyte.cloud/new_transaction")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Toast.makeText(ValidateActivity.this, "Couldn't send transaction, try again or reload application", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    ValidateActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // SUCCESS
                            MainActivity.Response response_json = gson.fromJson(myResponse, MainActivity.Response.class);
                            Intent i = new Intent(ValidateActivity.this, MainActivity.class);
                            i.putExtra("new_balance", response_json.new_balance);
                            startActivity(i);
                        }
                    });
                } else {
                    final String myResponse = response.body().string();
                    ValidateActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // ERROR
                            Toast.makeText(ValidateActivity.this, "Error", Toast.LENGTH_LONG).show();
                            System.out.println(myResponse);
                        }
                    });
                }
            }
        });
    }

    static class Transaction {
        int amount;
        String receiver;
        String sender;
        long time;

        public Transaction(int amount, String receiver, String sender){
            this.amount = amount;
            this.receiver = receiver;
            this.sender = sender;
            this.time = new Date().getTime();
        }
    }

    static class RequestTx {
        Transaction transaction;
        String signature;
        String public_key;

        public RequestTx(Transaction transaction, String signature, String public_key){
            this.transaction = transaction;
            this.signature = signature;
            this.public_key = public_key;
        }
    }
}