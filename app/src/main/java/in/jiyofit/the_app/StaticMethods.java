package in.jiyofit.the_app;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Date;

//Static methods can be called from anywhere after naming the class holding them
public class StaticMethods {

    public static void HideKeypad(Activity activity){
        //you need to pass activity to operate getcurrentfocus from a class
        InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //To prevent NPE if keyboard is already hidden.
        //to use this method in instances where there might not be focus (e.g. onPause(), etc
        inputManager.hideSoftInputFromWindow((null == activity.getCurrentFocus())
                ? null : activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static Boolean IsScreenLocked(Context ctx){
        KeyguardManager myKM = (KeyguardManager)ctx.getSystemService(Context.KEYGUARD_SERVICE);
        return myKM.inKeyguardRestrictedInputMode();
    }

    public static Boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Date convertDate(String dateInSeconds) {
        //Instagram stores values in seconds and not in milliseconds
        return new Date(Long.valueOf(dateInSeconds)*1000);
    }

    public static void playAudio (MediaPlayer mp, String type, Context ctx){
        SharedPreferences settingsPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        int vol = 100;
        if(type.equals("instruction")){
            vol = settingsPrefs.getInt("InstructionVolume", 100);
        } else if (type.equals("music")){
            vol = settingsPrefs.getInt("MusicVolume", 50);
        }
        float volume=(float)(1 - Math.log(100-vol)/Math.log(100));
        mp.start();
        mp.setVolume(volume, volume);
    }
}
