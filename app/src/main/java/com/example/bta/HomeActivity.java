package com.example.bta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.ActivityManager;
import android.widget.Toast;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_CODE = 0;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
       // requestReadAndSendSmsPermission();
        boolean perm=checkPermission();
        if(perm==false)
        {
            requestPermission();
        }
    }

    public void openAdmin(View view) {
        Intent mainIntent = new Intent(HomeActivity.this, AdminLogin.class);
        HomeActivity.this.startActivity(mainIntent);
        HomeActivity.this.finish();
    }

    public void openCustomer(View view) {
        Intent mainIntent = new Intent(HomeActivity.this, CustomerLogin.class);
        HomeActivity.this.startActivity(mainIntent);
        HomeActivity.this.finish();
    }


    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.READ_SMS) && ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.SEND_SMS) && ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.RECEIVE_SMS)) {

            return;
        }
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, SMS_PERMISSION_CODE);

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, READ_EXTERNAL_STORAGE)) {

            return;
        }
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, RequestPermissionCode);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

 /*   @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        super.onRequestPermissionsResult(RC, per, PResult);
        if (RC == SMS_PERMISSION_CODE) {

            if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(HomeActivity.this, "Permission Granted, Now your app can receive,read and send SMS.", Toast.LENGTH_SHORT).show();
                requestStoragePermission();
            } else {

                Toast.makeText(HomeActivity.this, "Permission Canceled, Now your application cannot  receive,read and send SMS.", Toast.LENGTH_SHORT).show();
                requestStoragePermission();
            }
        } else if (RC == RequestPermissionCode) {

            if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(HomeActivity.this, "Permission Granted, Now your application can access write to and read external storage", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(HomeActivity.this, "Permission Canceled, Now your application cannot write to and read external storage", Toast.LENGTH_SHORT).show();

            }

        }
    }

  */
    public void Exit(View view)
    {
        finish();
        System.exit(0);
    }
    private boolean checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(HomeActivity.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(HomeActivity.this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }

    }
    private void requestPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,MANAGE_EXTERNAL_STORAGE,READ_SMS}, SMS_PERMISSION_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Toast.makeText(this, "Android 11 or Above storage access granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Android 11 or above \n Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SMS_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                        Toast.makeText(HomeActivity.this, "Permission Granted, Now your application can access write to and read external storage", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeActivity.this, "Permission Not Granted, Your application cannot access write to and read external storage", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}