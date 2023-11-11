package com.example.bta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ExceptionDisplay extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception_display);
        //Toast.makeText(this, "I am ExceptionDisplay", Toast.LENGTH_LONG).show();
        TextView exception_text = findViewById(R.id.exception_text);
        Button btnBack = (Button) findViewById(R.id.btnBack);
        Bundle  bundle=getIntent().getExtras();
        if(bundle != null && bundle.containsKey("error"))
        {
            //exception_text.setText("Sorry an unexpected error occured \n Go back and try again");
           /* startActivity(
                    new Intent(Intent.ACTION_VIEW,
                            Uri.parse(
                                    String.format("https://api.whatsapp.com/send?phone=%s&text=%s","+25471799243",bundle.getString("error"))
                            )
                    )
            );
            */

            exception_text.setText(bundle.getString("error"));
            sendSMS(bundle.getString("error"));
            // Toast.makeText(getApplicationContext(), ""+bundle.getString("error"), Toast.LENGTH_LONG).show();

        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentData();
            }
        });
    }

    @Override
    public void onBackPressed() {
        intentData();
    }

    public void intentData() {

        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(ExceptionDisplay.this,MainActivity.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
    public boolean onKeyDown(int keyCode, KeyEvent e)
    {
        if((keyCode==KeyEvent.KEYCODE_BACK))
        {
            intentData();
        }



        return super.onKeyDown(keyCode,e);
    }
    protected void sendSMS(String mess) {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address" , new String ("0792244908"));
        smsIntent.putExtra("sms_body" , mess);
        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ExceptionDisplay.this,
                    "SMS faild, please try again later.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}





