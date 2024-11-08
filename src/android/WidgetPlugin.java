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

public class WidgetPlugin extends CordovaPlugin {
    private static final String TAG = "WidgetPlugin";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("updateWidget")) {
            String text = args.getString(0);
            this.updateWidget(text, callbackContext);
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
}
