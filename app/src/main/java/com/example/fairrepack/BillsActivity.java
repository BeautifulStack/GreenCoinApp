package com.example.fairrepack;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fairrepack.utils.Tx;
import com.example.fairrepack.utils.TxAdapter;
import com.example.fairrepack.utils.Wallet;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class BillsActivity extends AppCompatActivity {
    private ListView list_tx;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bills);

        list_tx = findViewById(R.id.list_tx);

        Context context = getApplicationContext();
        Wallet wallet = Wallet.get_wallet(context.getFilesDir().getPath(), context);
        address = wallet.getAddress();

        get_transactions();

    }

    private void get_transactions() {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://blockchain.octobyte.cloud/transactions/" + address)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Toast.makeText(BillsActivity.this, "Couldn't retrieve wallet balance", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    BillsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Response response_json = gson.fromJson(myResponse, Response.class);

                            TxAdapter adapter = new TxAdapter(BillsActivity.this, Arrays.asList(response_json.transactions), address);
                            list_tx.setAdapter(adapter);
                        }
                    });
                } else {
                    BillsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BillsActivity.this, "Couldn't retrieve wallet balance", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    static class Response {
        Tx[] transactions;
    }
}