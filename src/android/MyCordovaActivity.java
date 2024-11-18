package com.example;

import android.content.Intent;
import android.util.Log;
import org.apache.cordova.CordovaActivity;

public class MyCordovaActivity extends CordovaActivity {
    private static final String TAG = "MyCordovaActivity";

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "App removed from background");
        Context context = this.cordova.getActivity().getApplicationContext();
        Intent intent = new Intent("com.example.APP_CLOSED");
        intent.setPackage(getPackageName()); 
        context.sendBroadcast(intent);
    }
}
