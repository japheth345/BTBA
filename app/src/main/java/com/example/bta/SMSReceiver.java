package com.example.bta;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

public class SMSReceiver extends BroadcastReceiver{

    IntentFilter intentFilter;
    private static final String TAG = "MainActivity";
    private static final String PREF_USER_MOBILE_PHONE = "pref_user_mobile_phone";
    private static final int SMS_PERMISSION_CODE = 0;
    public  static final int RequestPermissionCode  = 1 ;
    Cursor cursor ;
    // String phone,message;
    //String mess;

    Context mcontext;
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();

        mcontext=context;
        String phone="";
        String message="";
        SmsMessage[] msgs = null;
        String str = "";
        if (bundle != null)
        {
            // Retrieve SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "\n";
            }
            // Display message
           // Toast.makeText(context, str, Toast.LENGTH_LONG).show();

            if(str.contains("Daraja-SandBox") || str.contains("Daraja") || str.contains("SandBox"))
            {
                Intent mainIntent = new Intent(context,MpesaActivity.class);
                mainIntent.putExtra("messo",str);
                context.startActivity(mainIntent);

            }
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms", str);
            context.sendBroadcast(broadcastIntent);

        }
    }

}
