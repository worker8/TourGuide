package tourguide.tourguidedemo;

import android.app.Activity;
import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.Display;
import android.view.View;

import tourguide.instrumentation.test.ToolTipMeasureTestActivity;

/**
 * Created by tanjunrong on 8/6/15.
 */
public class ToolTilMeasureTest extends ActivityInstrumentationTestCase2<ToolTipMeasureTestActivity> {
    ToolTipMeasureTestActivity mActivity;
    View mToolTip;
    public ToolTilMeasureTest() {
        super(ToolTipMeasureTestActivity.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Starts the activity under test using the default Intent with:
        // action = {@link Intent#ACTION_MAIN}
        // flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
        // All other fields are null or empty.
        mActivity = getActivity();
        mToolTip = mActivity.mTutorialHandler.getToolTip();
    }

    public void testNoCrash(){
//        mToolTip.performClick();
        Log.d("tourguide_test","height:" + mToolTip.getHeight());
        Log.d("tourguide_test","width:" + mToolTip.getWidth());
        Log.d("tourguide_test","getX:" + mToolTip.getX());
        Log.d("tourguide_test", "screen width:" + getScreenWidth(mActivity));
//        mToolTip.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                    @Override
//                    public void onGlobalLayout() {
//                        mToolTip.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    }
//                });
        boolean isOutOfLeftBound = mToolTip.getX() < 0;
        boolean isOutOfRightBound = (mToolTip.getX()+mToolTip.getWidth()) > getScreenWidth(mActivity);
        assertFalse("x of ToolTip is out of screen boundary", isOutOfLeftBound || isOutOfRightBound);

        boolean isOutOfTopBound = mToolTip.getY() < 0;
        boolean isOutOfBottomBound = (mToolTip.getY()+mToolTip.getHeight()) > getScreenHeight(mActivity);
        assertFalse("y of ToolTip is out of screen boundary", isOutOfTopBound || isOutOfBottomBound);
    }

    public int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }
    public int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

}
