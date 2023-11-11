package com.example.bta;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.Button;
import android.widget.Toast;

public class MessagesManager extends Service {
    Button btnSendSMS;
    IntentFilter intentFilter;
    private static final String TAG = "MainActivity";
    private static final String PREF_USER_MOBILE_PHONE = "pref_user_mobile_phone";
    private static final int SMS_PERMISSION_CODE = 0;
    public  static final int RequestPermissionCode  = 1 ;

    private AudioManager myAudioManager;

    Context mcontext;
    /** indicates how to behave if the service is killed */
    int mStartMode;
    /** interface for clients that bind */
    IBinder mBinder;
    /** indicates whether onRebind should be used */
    boolean mAllowRebind;
    String phone,message;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String mess = intent.getExtras().getString("sms");


           // phone = mess.substring(0, mess.indexOf(':'));
            //message = mess.substring((mess.indexOf(':')) + 1);
             Toast.makeText(getBaseContext(), "NEW MESSAGE DETAILS \n "+ mess, Toast.LENGTH_LONG).show();
            // int n=CountCommas(message);
            if(message.contains("Contact") || message.contains("Location") || message.contains("Sound") ||message.contains("Lock") )
            {


            }
            else
            {
                //  String num = SearchContact("Annie");
                Toast.makeText(context, "NEW MESSAGE NOT FOR MYHELPER  \n   Phone:  \t" + phone + "\n Message:\t" + message , Toast.LENGTH_LONG).show();


            }
        }};
    public MessagesManager()
    {
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
        mcontext=getBaseContext();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));

        registerReceiver(intentReceiver, intentFilter);


        // Toast.makeText(getBaseContext(), "Service has been created", Toast.LENGTH_SHORT).show();

    }


    /**
     * Runtime permission shenanigans
     */


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        Toast.makeText(getBaseContext(), "Service running......", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {

        unregisterReceiver(intentReceiver);
        super.onDestroy();
        Toast.makeText(getBaseContext(), "Service has been destroyed", Toast.LENGTH_SHORT).show();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }
    /** Called when a client is binding to the service with
     bindService()*/
    @Override
    public void onRebind(Intent intent) {
    }
    /** Called when The service is no longer used and is being destroyed
     */

}



