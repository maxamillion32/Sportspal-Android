package com.tanzil.sportspal.Utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arun on 22/4/15.
 */
public class Preferences {

    public static final String PREF_NAME = "stocoinData";


    public static final int MODE = Context.MODE_PRIVATE;
    //User Details
    public static final String LOGIN = "login";
    public static final String EMAIL = "email";

    public static final String REGISTRATION = "registration";
    public static final String USER_ID = "user_id";

    public static final String LOGOUT = "Logout";
    public static final String FORGET_PASS = "forget_pass";
    public static final String PASSWORD = "password";

    public static final String DEVICE_ID = "device_id";
    public static final String USER_TOKEN = "user_token";

    //  GpPreferences.writeString(getApplicationContext(), Preferences.NAME, "dev");
    // GpPreferences.readString(getApplicationContext(), Preferences.NAME, "");


    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    // execute at signup time.
    public static void clearAllPreference(Context context) {
       // ModelManager.getInstance().clearManagerInstance();
        getEditor(context).putString(LOGIN, null).commit();
        getEditor(context).putString(REGISTRATION, null).commit();
        getEditor(context).putString(USER_ID, null).commit();
    }


}

