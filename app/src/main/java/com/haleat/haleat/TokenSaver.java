package com.haleat.haleat;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Esta clase permite guardar el token y cogerlo cuando la aplicación lo necesite.
 */
public class TokenSaver {

    private final static String SHARED_PREF_NAME = "com.haleat.haleat.tokenDir";
    private final static String TOKEN_KEY = "com.haleat.haleat.tokenDir";

    public static String getToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, "");
    }

    public static void setToken(Context c, String token) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }
}
