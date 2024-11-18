package com.example;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.content.Context;
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
        Context context = this.getApplicationContext();
        Intent intent = new Intent("com.example.APP_KILLED");
        context.sendBroadcast(intent);
        super.onTaskRemoved(rootIntent);
    }
}
