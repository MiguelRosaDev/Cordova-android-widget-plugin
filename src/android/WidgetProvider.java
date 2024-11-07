package com.example;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.content.Intent;
import android.util.Log;
import android.content.ComponentName;

public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";
    private static final int APPWIDGET_TEXT_ID = 0x7f0a0001; // Hardcoded ID for appwidget_text

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, "");
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null && intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            String widgetText = intent.getStringExtra("widgetText");
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
            if (views != null) {
                views.setTextViewText(APPWIDGET_TEXT_ID, text);
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
}
