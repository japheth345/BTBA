package com.example.bta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.database.core.view.View;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    private static final int SMS_PERMISSION_CODE = 0;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));

        //startListening();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                finish();
                Intent mainIntent = new Intent(MainActivity.this,HomeActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }  }, SPLASH_DISPLAY_LENGTH);
    }



    private void  startListening() {
        Intent intent = new Intent(MainActivity.this, MessagesManager.class);
        startService(intent);
        Toast.makeText(getBaseContext(), "service has been started", Toast.LENGTH_SHORT).show();
    }

}