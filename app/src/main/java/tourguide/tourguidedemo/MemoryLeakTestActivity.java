package tourguide.tourguidedemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.leakcanary.RefWatcher;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * This activity is used for testing memory, it serves no demo purpose, hence it's not listed in TourGuideDemoMain activity (it's commented out)
 * To test this:
 * 1. uncomment MemoryLeakTestActivity in TourGuideDemoMain.java
 * 2. Then launch MemoryLeakTestActivity and click back, launch MemoryLeakTestActivity and click back, repeat many times
 * 3. Then look at the memory usage, also check if LeakCanary freezes the screen and log a memory heap dump
 * 4. To force a memory leak, comment 'onDetachedFromWindow()' method in FrameLayoutWithHole
 * TODO: this should be included as a test, rather than being a commented activity
 */
public class MemoryLeakTestActivity extends ActionBarActivity {
    public TourGuide mTutorialHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        Button button = (Button)findViewById(R.id.button);

        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip()
                            .setTitle("Hey!")
                            .setDescription("Let's hope that there's no memory leak..."))
                .setOverlay(new Overlay())
                .playOn(button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                           }
        });
    }
    @Override public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        Log.d("ddw","refWatcher: "+refWatcher);
        refWatcher.watch(this);
        refWatcher.watch(mTutorialHandler);
    }
}
