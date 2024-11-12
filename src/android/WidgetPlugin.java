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
    private static final String BUTTON_CLICKED_ACTION = "com.example.BUTTON_CLICKED";
    
    private CallbackContext buttonClickCallbackContext;
    private BroadcastReceiver buttonClickReceiver;

    @Override
    public void initialize(org.apache.cordova.CordovaInterface cordova, org.apache.cordova.CordovaWebView webView) {
        super.initialize(cordova, webView);
        registerButtonClickReceiver();
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
    
    private void registerButtonClickReceiver() {
        if (buttonClickReceiver == null) {
            buttonClickReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (BUTTON_CLICKED_ACTION.equals(intent.getAction())) {
                        Log.d(TAG, "Button clicked broadcast received in WidgetPlugin");
                        if (buttonClickCallbackContext != null) {
                            PluginResult result = new PluginResult(PluginResult.Status.OK);
                            result.setKeepCallback(true);
                            buttonClickCallbackContext.sendPluginResult(result);
                        }
                    }
                }
            };
            IntentFilter filter = new IntentFilter(BUTTON_CLICKED_ACTION);
            this.cordova.getActivity().registerReceiver(buttonClickReceiver, filter);
            Log.d(TAG, "Button click BroadcastReceiver registered in WidgetPlugin");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (buttonClickReceiver != null) {
            this.cordova.getActivity().unregisterReceiver(buttonClickReceiver);
            Log.d(TAG, "Button click BroadcastReceiver unregistered in WidgetPlugin");
        }
    }
}
