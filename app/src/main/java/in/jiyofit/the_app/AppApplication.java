package in.jiyofit.the_app;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AppApplication extends Application {
    private static AppApplication sInstance;
    public static final String INSTAGRAM_ACCESS_TOKEN = "abc";

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

    }

}

