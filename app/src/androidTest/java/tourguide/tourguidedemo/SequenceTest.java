package tourguide.tourguidedemo;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;

import tourguide.instrumentation.test.SequenceTestActivity;
import tourguide.tourguide.FrameLayoutWithHole;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.TourGuide;

/**
 * Created by tanjunrong on 8/6/15.
 */
public class SequenceTest extends ActivityInstrumentationTestCase2<SequenceTestActivity> {
    SequenceTestActivity mActivity;
    FrameLayoutWithHole mOverlay;
    Button button,button2, button3;
    final String CLASS_NAME = SequenceTest.this.getClass().getSimpleName();

    public SequenceTest() {
        super(SequenceTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(CLASS_NAME, CLASS_NAME + " started");

        // Starts the activity under test using the default Intent with:
        // action = {@link Intent#ACTION_MAIN}
        // flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
        // All other fields are null or empty.
        mActivity = getActivity();
        button = (Button)mActivity.findViewById(R.id.button);
        button2 = (Button)mActivity.findViewById(R.id.button2);
        button3 = (Button)mActivity.findViewById(R.id.button3);
    }

    /**
     * To verify if it proceeds to the next tourGuide
     */
    @UiThreadTest
    public void testTheSequence(){
        int size = mActivity.mSequence.getTourGuideArray().length;
        int currentSequence = 0;

        try {
            assertEquals(getActualSequence(), currentSequence); //to make sure it proceeds to the next sequence
            Thread.sleep(1500);
            button.performClick();
            currentSequence += 1;

            assertEquals(getActualSequence(), currentSequence); //to make sure it proceeds to the next sequence
            button2.performClick();
            currentSequence += 1;
            Thread.sleep(1500);

            assertEquals(getActualSequence(), currentSequence);//to make sure it proceeds to the next sequence
            button3.performClick();

            assertEquals(mActivity.mSequence.mCurrentSequence, size); //to check if it reaches to the end of tourguide
        }
        catch (InterruptedException e){

        }
    }

    /**
     * As the mCurrentSequence will be incremented by one after called PlayInSequence
     * @return ActualSequence
     */
    private int getActualSequence(){
        return mActivity.mSequence.mCurrentSequence-1;
    }

    @UiThreadTest
    public void testTourGuideSettings(){

        //overlay continue method
        if (SequenceTestActivity.CHOSEN_CONTINUE_METHOD == SequenceTestActivity.OVERLAY_METHOD) {
            Log.d(CLASS_NAME, "Method: Overlay");
            assertEquals(mActivity.mSequence.getContinueMethod(), Sequence.ContinueMethod.Overlay);

            TourGuide[] tourGuides = mActivity.mSequence.getTourGuideArray();
            runOverlayTest(tourGuides, getActualSequence());

            button.performClick();
            runOverlayTest(tourGuides, getActualSequence());

            button2.performClick();
            runOverlayTest(tourGuides, getActualSequence());

            button3.performClick();
        }
        //overlay listener continue method
        else if (SequenceTestActivity.CHOSEN_CONTINUE_METHOD == SequenceTestActivity.OVERLAY_LISTENER_METHOD) {
            Log.d(CLASS_NAME, "Method: Overlay Listener");
            assertEquals(mActivity.mSequence.getContinueMethod(), Sequence.ContinueMethod.OverlayListener);

            TourGuide[] tourGuides = mActivity.mSequence.getTourGuideArray();
            runOverlayListenerTest(tourGuides, getActualSequence());

            button.performClick();
            runOverlayListenerTest(tourGuides, getActualSequence());

            button2.performClick();
            runOverlayListenerTest(tourGuides, getActualSequence());

            button3.performClick();
        }
    }

    /**
     * Check Overlay
     * @param tourGuides
     * @param ActualSequence
     */
    private void runOverlayTest(TourGuide[] tourGuides, int ActualSequence){
        //check the priority of the overlay
        if (mActivity.mTutorialHandler.mOverlay!=null && tourGuides[ActualSequence].mOverlay!=null){
            Log.d(CLASS_NAME, "Overlay, Sequence "+ActualSequence+ " Set Individual Overlay");
            assertEquals(mActivity.mTutorialHandler.mOverlay, tourGuides[ActualSequence].mOverlay);
        }
        else if (mActivity.mTutorialHandler.mOverlay!=null && tourGuides[ActualSequence].mOverlay==null){
            Log.d(CLASS_NAME, "Overlay, Sequence "+ActualSequence+ " Set default Overlay");
            assertEquals(mActivity.mTutorialHandler.mOverlay, mActivity.mSequence.getDefaultOverlay());
        }

//        //check priority of overlay's listener
//        if (mActivity.mTourGuideHandler.mOverlay!=null && mActivity.mTourGuideHandler.mOverlay.mOnClickListener!=null
//                && tourGuides[ActualSequence].mOverlay!=null && tourGuides[ActualSequence].mOverlay.mOnClickListener!=null){
//            Log.d(CLASS_NAME, "Overlay, Sequence "+ActualSequence+" Set Individual Listener");
//            assertEquals(mActivity.mSequence.getOverlayListener(), tourGuides[ActualSequence].mOverlay.mOnClickListener);
//        }
//        else if (mActivity.mTourGuideHandler.mOverlay!=null && mActivity.mTourGuideHandler.mOverlay.mOnClickListener!=null
//                && tourGuides[ActualSequence].mOverlay!=null && tourGuides[ActualSequence].mOverlay.mOnClickListener==null){
//            Log.d(CLASS_NAME, "Overlay, Sequence "+ActualSequence+" Set default Listener");
//            assertEquals(mActivity.mSequence.getOverlayListener(), mActivity.mSequence.getDefaultOverlay().mOnClickListener);
//        }
//
//        else if (mActivity.mTourGuideHandler.mOverlay!=null && mActivity.mTourGuideHandler.mOverlay.mOnClickListener!=null
//                && tourGuides[ActualSequence].mOverlay==null){
//            Log.d(CLASS_NAME, "Overlay, Sequence "+ActualSequence+" Set default Listener");
//            assertEquals(mActivity.mSequence.getOverlayListener(), mActivity.mSequence.getDefaultOverlay().mOnClickListener);
//        }
    }

    /**
     * Check onClickListener and Overlay
     * @param tourGuides
     * @param ActualSequence
     */
    private static void runOverlayListenerTest(TourGuide[] tourGuides, int ActualSequence){
//        assertNotNull(mActivity.mTutorialHandler.mOverlay); //Overlay must not null
//        assertNotNull(mActivity.mSequence.getOverlayListener());//Overlay must have OnClickListener
//
//        //check the priority of the overlay
//        if (tourGuides[ActualSequence].mOverlay!=null){
//            Log.d(CLASS_NAME, "Overlay Listener, Sequence "+ActualSequence+" Set Individual Overlay");
//            assertEquals(mActivity.mTourGuideHandler.mOverlay, tourGuides[ActualSequence].mOverlay);
//        }
//        else if (tourGuides[ActualSequence].mOverlay==null){
//            Log.d(CLASS_NAME, "Overlay Listener, Sequence "+ActualSequence+" Set default Overlay");
//            assertEquals(mActivity.mTourGuideHandler.mOverlay, mActivity.mSequence.getDefaultOverlay());
//        }
//
//        //check overlay's listener: if default overlay listener is null, the overlay must not null.
//        if (mActivity.mSequence.getDefaultOverlay().mOnClickListener==null){
//            Log.d(CLASS_NAME, "Overlay Listener,  Sequence "+ActualSequence+" Overlay's listener is not empty");
//            assertNotNull(tourGuides[ActualSequence].mOverlay.mOnClickListener);
//        }
//
//        else{
//            Log.d(CLASS_NAME, "Overlay Listener,  Sequence "+ActualSequence+" Overlay's listener is set to default listener");
//            assertEquals(mActivity.mSequence.getOverlayListener(), mActivity.mSequence.getDefaultOverlay().mOnClickListener);
//        }

        //no need to do priority checking as it is only either overlay's listener or default listener
    }
}
