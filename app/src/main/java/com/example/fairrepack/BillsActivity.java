package com.example.fairrepack;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fairrepack.utils.Tx;
import com.example.fairrepack.utils.TxAdapter;
import com.example.fairrepack.utils.Wallet;

import java.util.ArrayList;

public class BillsActivity extends AppCompatActivity {
    private ListView list_tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bills);

        list_tx = findViewById(R.id.list_tx);

        TxAdapter adapter = new TxAdapter(BillsActivity.this, this.get_transactions());
        list_tx.setAdapter(adapter);

    }

    private ArrayList<Tx> get_transactions() {
        Context context = getApplicationContext();
        Wallet wallet = Wallet.get_wallet(context.getFilesDir().getPath(), context);


        return null;
    }
}