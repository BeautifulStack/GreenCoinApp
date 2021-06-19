package com.example.fairrepack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fairrepack.utils.Wallet;

public class WalletActivity extends AppCompatActivity {
    private TextView address;
    private ImageView delete_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        address = findViewById(R.id.address_wallet);
        delete_wallet = findViewById(R.id.delete_wallet);

        address.setText(getIntent().getStringExtra("address"));

        delete_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(WalletActivity.this);
                builder.setTitle("Delete actual wallet ?");
                builder.setMessage("Caution: You will lose any coins associated with this wallet FOREVER");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Wallet.delete_wallet(context)){
                            Intent i = new Intent(WalletActivity.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(WalletActivity.this, "Problem trying to delete wallet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}