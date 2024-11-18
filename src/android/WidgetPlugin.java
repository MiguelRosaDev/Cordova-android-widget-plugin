package com.example;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.cordova.PluginResult;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public class WidgetPlugin extends CordovaPlugin {
    private static final String TAG = "WidgetPlugin";
    private CallbackContext buttonClickCallbackContext;
    private BroadcastReceiver buttonClickReceiver;
    private static WidgetPlugin instance;

    public WidgetPlugin() {
        instance = this; 
    }

    public static WidgetPlugin getInstance() {
        return instance; 
    }
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("updateWidget")) {
            String text = args.getString(0);
            this.updateWidget(text, callbackContext);
            return true;
        } else if (action.equals("listenForButtonClicks")) {
            this.listenForButtonClicks(callbackContext);
            return true;
        }
        return false;
    }

    private void updateWidget(String text, CallbackContext callbackContext) {
        try {
            Context context = this.cordova.getActivity().getApplicationContext();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
            Intent intent = new Intent(context, WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra("widgetText", text);
            int[] ids = appWidgetManager.getAppWidgetIds(thisWidget);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
            Log.d(TAG, "Update widget intent sent with text: " + text);
            callbackContext.success("Widget update request sent successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error updating widget: " + e.getMessage());
            callbackContext.error("Failed to update widget: " + e.getMessage());
        }
    }
    
    private void listenForButtonClicks(CallbackContext callbackContext) {
        this.buttonClickCallbackContext = callbackContext;
        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
    }

    public void sendButtonClickEvent(String eventData) {
        if (buttonClickCallbackContext != null) {
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, eventData);
            pluginResult.setKeepCallback(true); // Mantém o callback ativo para futuros eventos
            buttonClickCallbackContext.sendPluginResult(pluginResult);
        }
    }
    
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        
        Log.d("MainActivity", "App removed from background");    
        Context context = this.cordova.getActivity().getApplicationContext();
        Intent intent = new Intent("com.example.APP_CLOSED");
        intent.setPackage(getPackageName()); 
        context.sendBroadcast(intent);
    }
}
