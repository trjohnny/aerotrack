package com.aerotrack.utils;

    import java.util.ResourceBundle;

public class ResourceHelper {
    private static final String BUNDLE_NAME = "ui_strings";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (Exception e) {
            return '!' + key + '!';
        }
    }
}