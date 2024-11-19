package com.example;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.util.Log;
import android.content.ComponentName;
import android.text.Html;

public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";
    public static final String BUTTON_CLICKED_ACTION = "com.example.BUTTON_CLICKED";
    public static final String UPDATE_ACTION = "com.example.UPDATE_WIDGET";
    public static final String ACTION_APP_CLOSED = "com.example.APP_CLOSED";
    public static final String ACTION_APPWIDGET_DELETED = "android.appwidget.action.APPWIDGET_DELETED";
    public static String localWidgetText = "Faça Login na App";
    private static boolean isFirstTime = true;
  
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate called");
        if (isFirstTime) {
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, localWidgetText);
            }
            isFirstTime = false;
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
        } else if (BUTTON_CLICKED_ACTION.equals(intent.getAction())) {
            Log.d(TAG, "Received widget button click");
            WidgetPlugin widgetInstance = WidgetPlugin.getInstance();
            if (widgetInstance != null) {
                widgetInstance.sendButtonClickEvent("Button Clicked");
            } else {
                Log.e(TAG, "WidgetPlugin instance is null");
            }
        } else if (ACTION_APP_CLOSED.equals(intent.getAction()) || ACTION_APPWIDGET_DELETED.equals(intent.getAction())) {
            Log.d(TAG, "App closed event received in widget");
            String widgetText = intent.getStringExtra("widgetText");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, localWidgetText);
            }
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String text) {
        try {
            RemoteViews views = new RemoteViews(context.getPackageName(), getResourceId(context, "widget_layout", "layout"));
            if (views != null && !text.trim().isEmpty()) {
                int textViewId = getResourceId(context, "appwidget_text", "id");
                
                String plainText = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString();
                
                views.setTextViewText(textViewId, plainText);
                //views.setTextColor(textViewId, 0xFFFFFFFF); // White text color

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
                
                // Set up the button click intent
                Intent buttonIntent = new Intent(context, WidgetProvider.class);
                buttonIntent.setAction(BUTTON_CLICKED_ACTION);
                PendingIntent buttonPendingIntent = PendingIntent.getBroadcast(context, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                views.setOnClickPendingIntent(getResourceId(context, "update_button", "id"), buttonPendingIntent);

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
