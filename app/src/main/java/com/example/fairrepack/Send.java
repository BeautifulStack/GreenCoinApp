package com.example.fairrepack;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Send extends AppCompatActivity {
    public static final int CAMERA_PERMISSION_CODE = 100;

    private Button camera;
    private String permission;
    private int requestCode;
    private Button scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        camera = findViewById(R.id.camera);
        scan = findViewById(R.id.scan);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Send.this,ScanActivity.class);
                startActivity(intent);
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermission(String permission, int requestCode){
        this.permission = permission;
        this.requestCode = requestCode;
        if(ContextCompat.checkSelfPermission(Send.this, permission)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(Send.this, new String[] {permission},
                    requestCode);
        }
        else {
            Toast.makeText(this,"Permission Already Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}