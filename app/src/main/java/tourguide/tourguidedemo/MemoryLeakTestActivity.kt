package tourguide.tourguidedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_basic.*
import tourguide.tourguide.Overlay
import tourguide.tourguide.Pointer
import tourguide.tourguide.TourGuide

/**
 * This activity is used for testing memory, it serves no demo purpose, hence it's not listed in TourGuideDemoMain activity (it's commented out)
 * To test this:
 * 1. uncomment MemoryLeakTestActivity in TourGuideDemoMain.java
 * 2. Then launch MemoryLeakTestActivity and click back, launch MemoryLeakTestActivity and click back, repeat many times
 * 3. Then look at the memory usage, also check if LeakCanary freezes the screen and log a memory heap dump
 * 4. To force a memory leak, comment 'onDetachedFromWindow()' method in FrameLayoutWithHole
 * TODO: this should be included as a test, rather than being a commented activity
 */
class MemoryLeakTestActivity : AppCompatActivity() {
    lateinit var tourGuide: TourGuide
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)

        tourGuide = TourGuide.create(this) {
            pointer { Pointer() }
            overlay { Overlay() }
            toolTip {
                title { "Hey!" }
                description { "Let's hope that there's no memory leak..." }
            }
        }.playOn(button1)

        button1.setOnClickListener { tourGuide.cleanUp() }
    }

    public override fun onDestroy() {
        super.onDestroy()
        MyApplication.getRefWatcher(this)?.also { _refWatcher ->
            _refWatcher.watch(this)
            _refWatcher.watch(tourGuide)
        }
    }
}
