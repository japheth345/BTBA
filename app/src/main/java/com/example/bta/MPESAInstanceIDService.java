package com.example.bta;

import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MPESAInstanceIDService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(MpesaActivity.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("InstanceID",s);
        editor.commit();
    }
}
