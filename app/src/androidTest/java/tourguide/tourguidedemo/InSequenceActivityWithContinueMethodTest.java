package tourguide.tourguidedemo;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;

import tourguide.instrumentation.test.InSequenceWithContinueMethodActivity;
import tourguide.tourguide.ContinueMethod;
import tourguide.tourguide.FrameLayoutWithHole;
import tourguide.tourguide.TourGuide;

/**
 * Created by tanjunrong on 8/6/15.
 */
public class InSequenceActivityWithContinueMethodTest extends ActivityInstrumentationTestCase2<InSequenceWithContinueMethodActivity> {
    InSequenceWithContinueMethodActivity mActivity;
    FrameLayoutWithHole mOverlay;
    Button button,button2, button3;
    String CLASS_NAME = InSequenceActivityWithContinueMethodTest.this.getClass().getSimpleName();
    public InSequenceActivityWithContinueMethodTest() {
        super(tourguide.instrumentation.test.InSequenceWithContinueMethodActivity.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(CLASS_NAME, "test started");

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
     * To verify if it proceed to the next tourguide
     */
    @UiThreadTest
    public void testTheSequence(){
        int Size = mActivity.seq.getTourGuideArray().length;
        int CurrentSequence = 0;

        try {
            assertEquals(getActualSequence(), CurrentSequence); //to make sure it proceeds to the next sequence
            Thread.sleep(1500);
            button.performClick();
            CurrentSequence +=1;


            assertEquals(getActualSequence(), CurrentSequence); //to make sure it proceeds to the next sequence
            button2.performClick();
            CurrentSequence +=1;
            Thread.sleep(1500);

            assertEquals(getActualSequence(), CurrentSequence);//to make sure it proceeds to the next sequence
            button3.performClick();

            assertEquals(mActivity.seq.mCurrentSequence, Size); //to check if it reaches to the end of tourguide
        }
        catch (InterruptedException e){

        }
    }

    /**
     * As the mCurrentSequence will be incremented by one after called PlayInSequence
     * @return ActualSequence
     */
    private int getActualSequence(){
        return mActivity.seq.mCurrentSequence-1;
    }

    @UiThreadTest
    public void testTourGuideSettings(){

        //overlay continue method
        if (InSequenceWithContinueMethodActivity.ChosenContinueMethod == InSequenceWithContinueMethodActivity.OVERLAY_METHOD) {
            Log.d(CLASS_NAME, "Method: Overlay");
            assertEquals(mActivity.seq.getContinueMethod(), ContinueMethod.Overlay);

            TourGuide[] tourGuides = mActivity.seq.getTourGuideArray();
            runOverlayTest(tourGuides, getActualSequence());

            button.performClick();
            runOverlayTest(tourGuides, getActualSequence());

            button2.performClick();
            runOverlayTest(tourGuides, getActualSequence());

            button3.performClick();
        }

        //overlay listener continue method
        else if (InSequenceWithContinueMethodActivity.ChosenContinueMethod == InSequenceWithContinueMethodActivity.OVERLAY_LISTENER_METHOD) {
            Log.d(CLASS_NAME, "Method: Overlay Listener");
            assertEquals(mActivity.seq.getContinueMethod(), ContinueMethod.OverlayListener);

            TourGuide[] tourGuides = mActivity.seq.getTourGuideArray();
            runOverlayListenerTest(tourGuides, getActualSequence());

            button.performClick();
            runOverlayListenerTest(tourGuides, getActualSequence());

            button2.performClick();
            runOverlayListenerTest(tourGuides, getActualSequence());

            button3.performClick();
        }

        //tooltip continue method
        else if (InSequenceWithContinueMethodActivity.ChosenContinueMethod == InSequenceWithContinueMethodActivity.TOOLTIP_METHOD) {
            Log.d(CLASS_NAME, "Method: Tooltip");
            assertEquals(mActivity.seq.getContinueMethod(), ContinueMethod.ToolTip);

            TourGuide[] tourGuides = mActivity.seq.getTourGuideArray();
            runTooltipTest(tourGuides, getActualSequence());

            button.performClick();
            runTooltipTest(tourGuides, getActualSequence());

            button2.performClick();
            runTooltipTest(tourGuides, getActualSequence());

            button3.performClick();
        }

        //tooltiplistener continue method
        else if (InSequenceWithContinueMethodActivity.ChosenContinueMethod == InSequenceWithContinueMethodActivity.TOOLTIP_LISTENER_METHOD) {
            Log.d(CLASS_NAME, "Method: Tooltip Listener");
            assertEquals(mActivity.seq.getContinueMethod(), ContinueMethod.ToolTipListener);

            TourGuide[] tourGuides = mActivity.seq.getTourGuideArray();
            runTooltipListenerTest(tourGuides, getActualSequence());

            button.performClick();
            runTooltipListenerTest(tourGuides, getActualSequence());

            button2.performClick();
            runTooltipListenerTest(tourGuides, getActualSequence());

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
            assertEquals(mActivity.mTutorialHandler.mOverlay, mActivity.seq.getDefaultOverlay());
        }

        //check priority of overlay's listener
        if (mActivity.mTutorialHandler.mOverlay!=null && mActivity.mTutorialHandler.mOverlay.mOnClickListener!=null
                && tourGuides[ActualSequence].mOverlay!=null && tourGuides[ActualSequence].mOverlay.mOnClickListener!=null){
            Log.d(CLASS_NAME, "Overlay, Sequence "+ActualSequence+" Set Individual Listener");
            assertEquals(mActivity.seq.getOverlayListener(), tourGuides[ActualSequence].mOverlay.mOnClickListener);
        }
        else if (mActivity.mTutorialHandler.mOverlay!=null && mActivity.mTutorialHandler.mOverlay.mOnClickListener!=null
                && tourGuides[ActualSequence].mOverlay!=null && tourGuides[ActualSequence].mOverlay.mOnClickListener==null){
            Log.d(CLASS_NAME, "Overlay, Sequence "+ActualSequence+" Set default Listener");
            assertEquals(mActivity.seq.getOverlayListener(), mActivity.seq.getDefaultOverlay().mOnClickListener);
        }

        else if (mActivity.mTutorialHandler.mOverlay!=null && mActivity.mTutorialHandler.mOverlay.mOnClickListener!=null
                && tourGuides[ActualSequence].mOverlay==null){
            Log.d(CLASS_NAME, "Overlay, Sequence "+ActualSequence+" Set default Listener");
            assertEquals(mActivity.seq.getOverlayListener(), mActivity.seq.getDefaultOverlay().mOnClickListener);
        }
    }


    /**
     * Check onClickListener and Overlay
     * @param tourGuides
     * @param ActualSequence
     */
    private void runOverlayListenerTest(TourGuide[] tourGuides, int ActualSequence){
        assertNotNull(mActivity.mTutorialHandler.mOverlay); //Overlay must not null
        assertNotNull(mActivity.seq.getOverlayListener());//Overlay must have OnClickListener

        //check the priority of the overlay
        if (tourGuides[ActualSequence].mOverlay!=null){
            Log.d(CLASS_NAME, "Overlay Listener, Sequence "+ActualSequence+" Set Individual Overlay");
            assertEquals(mActivity.mTutorialHandler.mOverlay, tourGuides[ActualSequence].mOverlay);
        }
        else if (tourGuides[ActualSequence].mOverlay==null){
            Log.d(CLASS_NAME, "Overlay Listener, Sequence "+ActualSequence+" Set default Overlay");
            assertEquals(mActivity.mTutorialHandler.mOverlay, mActivity.seq.getDefaultOverlay());
        }

        //check overlay's listener: if default overlay listener is null, the overlay must not null.
        if (mActivity.seq.getDefaultOverlay().mOnClickListener==null){
            Log.d(CLASS_NAME, "Overlay Listener,  Sequence "+ActualSequence+" Overlay's listener is not empty");
            assertNotNull(tourGuides[ActualSequence].mOverlay.mOnClickListener);
        }

        else{
            Log.d(CLASS_NAME, "Overlay Listener,  Sequence "+ActualSequence+" Overlay's listener is set to default listener");
            assertEquals(mActivity.seq.getOverlayListener(), mActivity.seq.getDefaultOverlay().mOnClickListener);
        }

        //no need to do priority checking as it is only either overlay's listener or default listener
    }

    /**
     * Check Tooltip
     * @param tourGuides
     * @param ActualSequence
     */
    private void runTooltipTest(TourGuide[] tourGuides, int ActualSequence){
        //check the priority of the tooltip
        if (mActivity.mTutorialHandler.mToolTip!=null && tourGuides[ActualSequence].mToolTip!=null){
            Log.d(CLASS_NAME, "Tooltip,  Sequence "+ActualSequence+" Set Individual Tooltip");
            assertEquals(mActivity.mTutorialHandler.mToolTip, tourGuides[ActualSequence].mToolTip);
        }
        else if (mActivity.mTutorialHandler.mToolTip!=null && tourGuides[ActualSequence].mToolTip==null){
            Log.d(CLASS_NAME, "Tooltip,  Sequence "+ActualSequence+" Set default Tooltip");
            assertEquals(mActivity.mTutorialHandler.mToolTip, mActivity.seq.getDefaultToolTip());
        }

        //check priority of tooltip's listener
        if (mActivity.mTutorialHandler.mToolTip!=null && mActivity.mTutorialHandler.mToolTip.mOnClickListener!=null
                && tourGuides[ActualSequence].mToolTip!=null && tourGuides[ActualSequence].mToolTip.mOnClickListener!=null){
            Log.d(CLASS_NAME, "Tooltip,  Sequence "+ActualSequence+" Set Individual listener");
            assertEquals(mActivity.seq.getToolTipListener(), tourGuides[ActualSequence].mToolTip.mOnClickListener);
        }
        else if (mActivity.mTutorialHandler.mToolTip!=null && mActivity.mTutorialHandler.mToolTip.mOnClickListener!=null
                && tourGuides[ActualSequence].mToolTip!=null && tourGuides[ActualSequence].mToolTip.mOnClickListener==null){
            Log.d(CLASS_NAME, "Tooltip,  Sequence "+ActualSequence+" Set default listener");
            assertEquals(mActivity.seq.getToolTipListener(), mActivity.seq.getDefaultToolTip().mOnClickListener);
        }

        else if (mActivity.mTutorialHandler.mToolTip!=null && mActivity.mTutorialHandler.mToolTip.mOnClickListener==null
                && tourGuides[ActualSequence].mToolTip!=null && tourGuides[ActualSequence].mToolTip.mOnClickListener==null){
            Log.d(CLASS_NAME, "Tooltip,  Sequence "+ActualSequence+" Set default listener");
            assertEquals(mActivity.seq.getToolTipListener(), mActivity.seq.getDefaultToolTip().mOnClickListener);
        }

        else if (mActivity.mTutorialHandler.mToolTip!=null && mActivity.mTutorialHandler.mToolTip.mOnClickListener!=null
                && tourGuides[ActualSequence].mToolTip==null){
            Log.d(CLASS_NAME, "Tooltip, Sequence "+ActualSequence+" Set default Listener");
            assertEquals(mActivity.seq.getToolTipListener(), mActivity.seq.getDefaultToolTip().mOnClickListener);
        }
    }


    /**
     * Check onClickListener and Tooltip
     * @param tourGuides
     * @param ActualSequence
     */
    private void runTooltipListenerTest(TourGuide[] tourGuides, int ActualSequence){
        assertNotNull(mActivity.mTutorialHandler.mToolTip); //tooltip must not null
        assertNotNull(mActivity.seq.getToolTipListener());//tooltip must have OnClickListener

        //check the priority of the tooltip
        if (tourGuides[ActualSequence].mToolTip!=null){
            Log.d(CLASS_NAME, "Tooltip,  Sequence "+ActualSequence+" Set individual Tooltip");
            assertEquals(mActivity.mTutorialHandler.mToolTip, tourGuides[ActualSequence].mToolTip);
        }
        else if (tourGuides[ActualSequence].mToolTip==null){
            Log.d(CLASS_NAME, "Tooltip,  Sequence "+ActualSequence+" Set default Tooltip");
            assertEquals(mActivity.mTutorialHandler.mToolTip, mActivity.seq.getDefaultToolTip());
        }

        //check tooltip's listener: if default tooltip listener is null, the tooltip must not null.
        if (mActivity.seq.getDefaultToolTip().mOnClickListener==null){
            Log.d(CLASS_NAME, "Tooltip,  Sequence "+ActualSequence+" Tooltip's listener is not empty");
            assertNotNull(tourGuides[ActualSequence].mToolTip.mOnClickListener);
        }

        else{
            Log.d(CLASS_NAME, "Tooltip Listener,  Sequence "+ActualSequence+" Tooltip's listener is set to default listener");
            assertEquals(mActivity.seq.getToolTipListener(), mActivity.seq.getDefaultToolTip().mOnClickListener);
        }


        //no need to do priority checking as it is only either tooltip's listener or default listener
    }
}
