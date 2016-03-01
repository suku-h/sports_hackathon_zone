package in.jiyofit.the_app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AppApplication extends Application {
    private static AppApplication sInstance;
    public static final String INSTAGRAM_ACCESS_TOKEN = "2698952081.ea1b6e3.9c9e5541abd44a3da1fd7bd4d991da40";

    public static AppApplication getsInstance(){
        return  sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //thus sInstance assumes the application instance
        sInstance = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/kundli_hindi_normal.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build()
            );
        printHashKey();
    }


    public void printHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "in.jiyofit.the_app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Yeppa123:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
    }
}

