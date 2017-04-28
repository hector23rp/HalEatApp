package com.lpro.haleater;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Esta clase permite guardar el token y cogerlo cuando la aplicación lo necesite, y comprobar si un usuario tiene una sesión iniciada.
 */
public class TokenSaver {

    private final static String SHARED_PREF_TOKEN = "com.haleat.haleat.tokenDir";
    private final static String TOKEN_KEY = "com.haleat.haleat.tokenDir";
    private final static String SHARED_PREF_REMEMBER = "com.haleat.haleat.rememberDir";
    private final static String REMEMBER_KEY = "com.haleat.haleat.rememberDir";
    private final static String FILENAME = "remember.txt";
    private static File file = null;

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

    public static void setRemember(Context c, String value) throws IOException {
        FileOutputStream fos = c.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(value.getBytes());
        fos.close();
    }

    public static boolean checkRemember(Context c) {
        try {
            StringBuffer datax = new StringBuffer("");
            FileInputStream input = c.openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader ( input ) ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;
            String readString = buffreader.readLine ( ) ;
            while ( readString != null ) {
                datax.append(readString);
                readString = buffreader.readLine ( ) ;
            }
            isr.close ( ) ;
            String check = datax.toString();
            if(check.equals("Logueado")){
                return true;
            }
        }catch (IOException e){
        }

        return false;
    }
    public static void init(){
        if(file==null){
            file = new File(FILENAME);
        }
    }
}
