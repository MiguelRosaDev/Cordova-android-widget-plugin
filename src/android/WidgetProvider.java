package com.example;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.util.Log;
import android.content.ComponentName;
import android.content.SharedPreferences;

public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";
    private static final String PREFS_NAME = "com.example.WidgetProvider";  
    private static final String PREFS_KEY_TEXT = "widgetText";  


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate called");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, "Widget Initialized");
        }
    }

    //First time you load the widget on android home screen
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "onEnabled called");
        
        storeWidgetText(context, "Por favor faça login na App MyNOS");
        
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, "Por favor faça login na App MyNOS");
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive called with action: " + intent.getAction());
        if (intent.getAction() != null && intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            String widgetText = intent.getStringExtra("widgetText");
            Log.d(TAG, "Received widget text: " + widgetText);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, widgetText);
            }
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String text) {
        try {
            RemoteViews views = new RemoteViews(context.getPackageName(), getResourceId(context, "widget_layout", "layout"));
            if (views != null && text != null) {
                int textViewId = getResourceId(context, "appwidget_text", "id");
                views.setTextViewText(textViewId, text);
                views.setTextColor(textViewId, 0xFFFFFFFF); // White text color

                // Set the background image
                int logoResourceId = getResourceId(context, "logo", "drawable");
                if (logoResourceId != 0) {
                    views.setImageViewResource(getResourceId(context, "widget_background", "id"), logoResourceId);
                }
                
                // Create an Intent to launch the app
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    views.setOnClickPendingIntent(getResourceId(context, "widget_layout", "id"), pendingIntent);
                }

                appWidgetManager.updateAppWidget(appWidgetId, views);
                Log.d(TAG, "Widget updated successfully with text: " + text);
            } else {
                Log.e(TAG, "Failed to create RemoteViews");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating widget: " + e.getMessage());
        }
    }

    private static int getResourceId(Context context, String name, String type) {
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }

    private String getStoredWidgetText(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PREFS_KEY_TEXT, "Por favor faça login na App MyNOS"); 
    }

    private void storeWidgetText(Context context, String text) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_KEY_TEXT, text);
        editor.apply();
    }
}
