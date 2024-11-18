package com.example;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AppKilledService extends Service {
    private static final String TAG = "AppKilledService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "App killed: task removed");
        // Broadcast an intent that our plugin can listen for
        Intent intent = new Intent("com.example.APP_KILLED");
        sendBroadcast(intent);
        super.onTaskRemoved(rootIntent);
    }
}
