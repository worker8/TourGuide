package tourguide.tourguidedemo;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by tanjunrong on 7/3/15.
 */
public class MyApplication extends Application {
    private RefWatcher _refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application._refWatcher;
    }

    @Override public void onCreate() {
        super.onCreate();
        /* This is for checking memory leak: https://github.com/square/leakcanary */
        _refWatcher = LeakCanary.install(this);
    }
}
