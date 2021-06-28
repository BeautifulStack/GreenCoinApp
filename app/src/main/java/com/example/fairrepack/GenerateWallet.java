package com.example.fairrepack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fairrepack.utils.Wallet;

public class GenerateWallet extends AppCompatActivity {
    private Button import_btn, generate_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generatewallet);

        generate_btn = findViewById(R.id.generate_btn);

        this.generate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wallet wallet = Wallet.get_wallet(null, getApplicationContext());
                if (wallet == null) {
                    Toast.makeText(GenerateWallet.this, "Error generating wallet, try again", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(GenerateWallet.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        this.import_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Do import wallet from private key file
            }
        });

    }
}