package com.example;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class WidgetPlugin extends CordovaPlugin {
    private static final String TAG = "WidgetPlugin";
    private CallbackContext buttonClickCallbackContext;

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
        cordova.getThreadPool().execute(() -> {
            try {
                Intent intent = new Intent(cordova.getActivity(), WidgetProvider.class);
                intent.setAction(WidgetProvider.UPDATE_ACTION);
                intent.putExtra("widgetText", text);
                cordova.getActivity().sendBroadcast(intent);
                callbackContext.success("Widget updated successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error updating widget: " + e.getMessage());
                callbackContext.error("Failed to update widget: " + e.getMessage());
            }
        });
    }

    private void listenForButtonClicks(CallbackContext callbackContext) {
        this.buttonClickCallbackContext = callbackContext;
        IntentFilter filter = new IntentFilter(WidgetProvider.BUTTON_CLICKED_ACTION);
        cordova.getActivity().registerReceiver(buttonClickReceiver, filter);
        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
    }

    private BroadcastReceiver buttonClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WidgetProvider.BUTTON_CLICKED_ACTION.equals(intent.getAction())) {
                if (buttonClickCallbackContext != null) {
                    PluginResult result = new PluginResult(PluginResult.Status.OK, "Button clicked");
                    result.setKeepCallback(true);
                    buttonClickCallbackContext.sendPluginResult(result);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (buttonClickReceiver != null) {
            cordova.getActivity().unregisterReceiver(buttonClickReceiver);
        }
    }
}
