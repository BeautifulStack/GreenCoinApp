package com.example.fairrepack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.MediaType;
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

        //MediaType JSON = MediaType.get("application/json; charset=utf-8");
        //RequestBody body = RequestBody.create(json, JSON);

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
                
            }
        });
    }
}