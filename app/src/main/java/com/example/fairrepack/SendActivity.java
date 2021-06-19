package com.example.fairrepack;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendActivity extends AppCompatActivity implements View.OnClickListener {
    int max_amount = 0;
    private Button scan;
    private Button send_btn;
    private EditText address;
    private EditText amount;
    private TextView max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        scan = findViewById(R.id.scan);
        address = findViewById(R.id.address);
        amount = findViewById(R.id.amount);
        max = findViewById(R.id.max_amount);
        send_btn = findViewById(R.id.send_btn);

        max_amount = getIntent().getIntExtra("max", 0);
        max.setText("Max: "+max_amount);

        scan.setOnClickListener(this);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = amount.getText().toString();
                if (tmp.equals("")) {
                    Toast.makeText(SendActivity.this, "Invalid transfer amount !", Toast.LENGTH_LONG).show();
                } else {
                    int amount_tx = Integer.parseInt(tmp);
                    if (amount_tx > max_amount || amount_tx == 0) {
                        Toast.makeText(SendActivity.this, "Not enough funds !", Toast.LENGTH_LONG).show();
                    } else {
                        String address_tx = address.getText().toString();
                        Pattern pattern = Pattern.compile("[a-fA-F0-9]{40}");
                        Matcher matcher = pattern.matcher(address_tx);
                        if (matcher.find()){
                            Intent intent = new Intent(SendActivity.this, ValidateActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SendActivity.this, "Invalid receiver address !", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        scanCode();
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning code...");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                address.setText(result.getContents());
            } else {
                Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}