package tourguide.tourguidedemo

import android.app.Application
import android.content.Context

import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

class MyApplication : Application() {
    private var _refWatcher: RefWatcher? = null

    override fun onCreate() {
        super.onCreate()
        /* This is for checking memory leak: https://github.com/square/leakcanary */
        _refWatcher = LeakCanary.install(this)
    }

    companion object {

        fun getRefWatcher(context: Context): RefWatcher? {
            val application = context.applicationContext as MyApplication
            return application._refWatcher
        }
    }
}
