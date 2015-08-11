package tourguide.tourguidedemo;

        import android.test.ActivityInstrumentationTestCase2;

        import tourguide.tourguide.FrameLayoutWithHole;

/**
 * Created by tanjunrong on 8/6/15.
 */
public class BasicActivityCrashTest extends ActivityInstrumentationTestCase2<BasicActivity> {
    BasicActivity mActivity;
    FrameLayoutWithHole mOverlay;
    public BasicActivityCrashTest() {
        super(BasicActivity.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Starts the activity under test using the default Intent with:
        // action = {@link Intent#ACTION_MAIN}
        // flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
        // All other fields are null or empty.
        mActivity = getActivity();
        mOverlay = mActivity.mTutorialHandler.getOverlay();
    }

    public void testNoCrash(){
        mOverlay.performClick();
        assertTrue("There is no crash :)", true);
    }
}
