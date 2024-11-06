# Cordova Android Widget Plugin

This Cordova plugin allows you to create and update an Android home screen widget from your Cordova application. The widget displays dynamic text that can be updated from your app.

## Installation

To install this plugin in your Cordova project, use the following command:

```bash
cordova plugin add https://github.com/yourusername/cordova-plugin-android-widget.git
```

Or, if you're installing from a local copy:

```bash
cordova plugin add /path/to/your-cordova-widget-plugin
```

## Usage

Once the plugin is installed, you can use it in your JavaScript code as follows:

```javascript
cordova.plugins.androidWidget.updateWidget("Hello, Widget!", 
    function(success) { console.log(success); },
    function(error) { console.error(error); }
);
```

This will update the widget with the text "Hello, Widget!".

## API

### updateWidget

Updates the text displayed in the widget.

```javascript
cordova.plugins.androidWidget.updateWidget(text, successCallback, errorCallback)
```

- `text` (String): The text to display in the widget.
- `successCallback` (Function): Called when the widget is successfully updated.
- `errorCallback` (Function): Called if there's an error updating the widget.

## Customization

### Widget Layout

The widget layout is defined in `src/android/widget_layout.xml`. You can modify this file to change the appearance of your widget.

### Widget Update Frequency

By default, the widget updates every 24 hours. You can change this by modifying the `android:updatePeriodMillis` attribute in `src/android/widget_info.xml`.

### Widget Size

The default widget size is set to 40dp x 40dp. You can adjust this by modifying the `android:minWidth` and `android:minHeight` attributes in `src/android/widget_info.xml`.

## Requirements

- Cordova 5.0.0 or higher
- Android 4.0.3 or higher

## Known Issues

- The widget may not update immediately on some devices. This is due to how Android handles widget updates.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License.

## Support

If you're having any problem, please raise an issue on GitHub and we'll be happy to help.

```

Remember to replace `https://github.com/yourusername/cordova-plugin-android-widget.git` with the actual repository URL where you'll host this plugin.
```

This README provides a comprehensive guide for users of your plugin. It includes installation instructions, 
