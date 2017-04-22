package com.haleat.haleat;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Esta clase permite guardar el token y cogerlo cuando la aplicación lo necesite, y comprobar si un usuario tiene una sesión iniciada.
 */
public class TokenSaver {

    private final static String SHARED_PREF_TOKEN = "com.haleat.haleat.tokenDir";
    private final static String TOKEN_KEY = "com.haleat.haleat.tokenDir";
    private final static String SHARED_PREF_REMEMBER = "com.haleat.haleat.rememberDir";
    private final static String REMEMBER_KEY = "com.haleat.haleat.rememberDir";

    public static String getToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, "");
    }

    public static void setToken(Context c, String token) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public static void setRemember(Context c, int value){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_REMEMBER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(REMEMBER_KEY, value);
        editor.apply();
    }

    public static boolean checkRemember(Context c){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        int check = prefs.getInt(REMEMBER_KEY,0);
        if(check != 1){
            return false;
        }
        return true;
    }
}
