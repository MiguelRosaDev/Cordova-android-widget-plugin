
# Android Cordova Widget Plugin

This project implements a **Cordova plugin** that integrates with **Android Widgets**. 
The plugin provides functionalities to:

1. Update widget text and layout directly from JavaScript.
2. Receive button click events from the widget and forward them to the Cordova app.

## Architecture

- **WidgetPlugin**: The Cordova plugin facilitating communication between JavaScript and the Android widget.
- **WidgetProvider**: Implements the Android widget using the `AppWidgetProvider` class and handles the user interface on the widget.

---

## Features

- **Widget Updates**: Allows updating the widget text and layout via commands sent from the Cordova app.
- **Event Listening**: Receives button clicks from the widget and forwards them to the Cordova app.
- **Dynamic Configuration**: Direct integration between `WidgetPlugin` and `WidgetProvider`.

---

## Installation

1. Clone this repository to your local environment:
   ```bash
   git clone https://github.com/YOUR_USERNAME/android-cordova-widget-plugin.git
   cd android-cordova-widget-plugin
   ```

2. Add the plugin to your Cordova project:
   ```bash
   cordova plugin add ./path-to-plugin
   ```

3. Ensure the app has widget support and the necessary permissions in the `AndroidManifest.xml` file:
   ```xml
   <receiver
       android:name="com.example.WidgetProvider"
       android:exported="true">
       <intent-filter>
           <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
       </intent-filter>
       <meta-data
           android:name="android.appwidget.provider"
           android:resource="@xml/widget_provider_info" />
   </receiver>
   ```

4. Add the widget layout in `res/layout/widget_layout.xml`:
   ```xml
   <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:id="@+id/widget_layout"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:background="@drawable/widget_background">

       <TextView
           android:id="@+id/appwidget_text"
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="Initial Text"
           android:textColor="#FFFFFF"
           android:layout_centerHorizontal="true" />

       <ImageView
           android:id="@+id/update_button"
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:src="@drawable/ic_update"
           android:layout_below="@id/appwidget_text"
           android:layout_centerHorizontal="true" />
   </RelativeLayout>
   ```

---

## Usage

### 1. Update the Widget
On the JavaScript side, invoke the `updateWidget` method to update the widget text:
```javascript
cordova.plugins.androidWidget.updateWidget("Widget Text", function(success) {
    console.log("Widget updated successfully!");
}, function(error) {
    console.error("Error updating widget:", error);
});
```

### 2. Listen for Widget Events
Register a listener for widget button click events:
```javascript
cordova.plugins.androidWidget.listenForButtonClicks(function(eventData) {
    console.log("Event received from widget:", eventData);
}, function(error) {
    console.error("Error registering listener:", error);
});
```

---

## Components

- **WidgetProvider**:
  - Extends `AppWidgetProvider` to manage the widget lifecycle.
  - Receives button clicks and sends events to the `WidgetPlugin`.

- **WidgetPlugin**:
  - Extends `CordovaPlugin` to connect JavaScript to Android.
  - Implements methods:
    - `updateWidget`: Updates the widget layout and text.
    - `listenForButtonClicks`: Keeps a callback active for sending click events.

---

## Contributing

1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b my-feature
   ```
3. Commit your changes:
   ```bash
   git commit -m "Description of the new feature"
   ```
4. Push to your branch:
   ```bash
   git push origin my-feature
   ```
5. Open a Pull Request.

---

## License

This project is licensed under the [MIT License](LICENSE).

---

## Contact

For questions or suggestions, open an [Issue](https://github.com/MiguelRosaDev/Cordova-android-widget-plugin)
