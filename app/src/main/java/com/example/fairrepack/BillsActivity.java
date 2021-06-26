package com.example.fairrepack;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fairrepack.utils.Tx;
import com.example.fairrepack.utils.TxAdapter;
import com.example.fairrepack.utils.Wallet;
import com.google.gson.Gson;

import java.util.Arrays;

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

        TxAdapter adapter = new TxAdapter(BillsActivity.this, Arrays.asList(this.get_transactions()), address);
        list_tx.setAdapter(adapter);

    }

    private Tx[] get_transactions() {
        String response = "{'transactions':[{'amount':500,'receiver':'54c296f64af107a3ce15fc5ad7ab5e983768246f','sender':'REWARD','time':1},{'amount':500,'receiver':'54c296f64af107a3ce15fc5ad7ab5e983768246f','sender':'f4d548sg4sed6g8s654dfg6sed85g46se85gs6ezd','time':1},{'amount':500,'receiver':'f4d548sg4sed6g8s654dfg6sed85g46se85gs6ezd','sender':'54c296f64af107a3ce15fc5ad7ab5e983768246f','time':1}]}";

        Gson gson = new Gson();
        Response response1 = gson.fromJson(response, Response.class);

        for (int i=0; i<response1.transactions.length; i++) {
            System.out.println(response1.transactions[i].getReceiver());
        }

        return response1.transactions;
    }

    static class Response {
        Tx[] transactions;
    }
}