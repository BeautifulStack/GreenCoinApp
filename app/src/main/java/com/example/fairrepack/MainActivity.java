package com.example.fairrepack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private CardView bills;
    private ImageView profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bills = (CardView) findViewById(R.id.bills);
        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        profil = (ImageView) findViewById(R.id.profil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });
    }

    public void openActivity2() {
        Intent intent = new Intent(this, Bills.class);
        startActivity(intent);
    }

    public void openActivity3() {
        Intent intent = new Intent(this, Profil.class);
        startActivity(intent);
    }
}