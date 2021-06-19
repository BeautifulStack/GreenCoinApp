package com.example.fairrepack;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WalletActivity extends AppCompatActivity {
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        address = findViewById(R.id.address_wallet);
        address.setText(getIntent().getStringExtra("address"));
    }
}