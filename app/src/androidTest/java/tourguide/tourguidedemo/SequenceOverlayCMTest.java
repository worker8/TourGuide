package tourguide.tourguidedemo;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;

import tourguide.instrumentation.test.SequenceOverlayCMTestActivity;

/**
 * Created by tanjunrong on 8/6/15.
 */
public class SequenceOverlayCMTest extends ActivityInstrumentationTestCase2<SequenceOverlayCMTestActivity> {
    final String CLASS_NAME = SequenceOverlayCMTest.this.getClass().getSimpleName();
    SequenceOverlayCMTestActivity mActivity;

    public SequenceOverlayCMTest() {
        super(SequenceOverlayCMTestActivity.class);
    }
    @Override @UiThreadTest
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(CLASS_NAME, CLASS_NAME + " started");
        mActivity = getActivity();
    }

    @UiThreadTest
    public void testTheSequence(){

        assertEquals("ToolTip #1 title mismatch", SequenceOverlayCMTestActivity.TEST_TITLE1, mActivity.mSequenceManagerTG.mToolTip.mTitle);

        mActivity.mSequenceManagerTG.getOverlay().performClick();
        assertEquals("ToolTip #2 title mismatch", SequenceOverlayCMTestActivity.TEST_TITLE2, mActivity.mSequenceManagerTG.mToolTip.mTitle);

        mActivity.mSequenceManagerTG.getOverlay().performClick();
        assertEquals("ToolTip #3 title mismatch", SequenceOverlayCMTestActivity.TEST_TITLE3, mActivity.mSequenceManagerTG.mToolTip.mTitle);
    }

    @UiThreadTest
    public void testDefaultOverlay(){

        assertEquals("#1 Overlay should be same as default overlay", mActivity.mDefaultOverlay, mActivity.mSequenceManagerTG.mOverlay);

        mActivity.mSequenceManagerTG.getOverlay().performClick();
        assertNotSame("#2 Overlay should NOT be same as default overlay", mActivity.mDefaultOverlay, mActivity.mSequenceManagerTG.mOverlay);

        mActivity.mSequenceManagerTG.getOverlay().performClick();
        assertEquals("#3 Overlay should be same as default overlay", mActivity.mDefaultOverlay, mActivity.mSequenceManagerTG.mOverlay);
    }

//    @UiThreadTest
//    /* When ContinueMethod is set as Overlay, listener should not be set, if it's set, exception should be thrown */
//    public void testExceptionWhenOverlayCM_And_ListenerExist(){
//
//        boolean isCrash = false;
//
//        // assign an empty listener to test if it will generate exception
//        mActivity.mDefaultOverlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // do nothing, just for test purpose
//            }
//        });
//
//        try {
//            buildSequence();
//        } catch (IllegalArgumentException e) {
//            isCrash = true;
//        }
//
//        assertEquals("ContinueMethod.Overlay is set, default Overlay Listener is also set, it should crash with IllegalArgumentException, but didn't", true, isCrash);
//    }
}
