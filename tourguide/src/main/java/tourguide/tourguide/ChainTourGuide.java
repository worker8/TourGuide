package tourguide.tourguide;

import android.app.Activity;
import android.view.View;

/**
 * {@link ChainTourGuide} is designed to be used with {@link Sequence}. The purpose is to run TourGuide in a series.
 * {@link ChainTourGuide} extends from {@link TourGuide} with extra capability to be run in sequence.
 * Check OverlaySequenceTourActivity.java in the Example of TourGuide to learn how to use.
 */
public class ChainTourGuide extends TourGuide {
    private Sequence mSequence;

    public ChainTourGuide(Activity activity) {
        super(activity);
    }

    /* Static builder */
    public static ChainTourGuide init(Activity activity){
        return new ChainTourGuide(activity);
    }

    @Override
    public TourGuide playOn(View targetView) {
        throw new RuntimeException("playOn() should not be called ChainTourGuide, ChainTourGuide is meant to be used with Sequence. Use TourGuide class for playOn() for single TourGuide. Only use ChainTourGuide if you intend to run TourGuide in consecutively.");
    }

    public ChainTourGuide playLater(View view){
        mHighlightedView = view;
        return this;
    }

    @Override
    public ChainTourGuide with(Technique technique) {
        return (ChainTourGuide)super.with(technique);
    }

    @Override
    public ChainTourGuide motionType(MotionType motionType) {
        return (ChainTourGuide)super.motionType(motionType);
    }

    @Override
    public ChainTourGuide setOverlay(Overlay overlay) {
        return (ChainTourGuide)super.setOverlay(overlay);
    }

    @Override
    public ChainTourGuide setToolTip(ToolTip toolTip) {
        return (ChainTourGuide)super.setToolTip(toolTip);
    }

    @Override
    public ChainTourGuide setPointer(Pointer pointer) {
        return (ChainTourGuide)super.setPointer(pointer);
    }

    public ChainTourGuide next(){
        if (mFrameLayout!=null) {
            cleanUp();
        }

        if (mSequence.mCurrentSequence < mSequence.mTourGuideArray.length) {
            setToolTip(mSequence.getToolTip());
            setPointer(mSequence.getPointer());
            setOverlay(mSequence.getOverlay());

            mHighlightedView = mSequence.getNextTourGuide().mHighlightedView;

            setupView();
            mSequence.mCurrentSequence++;
        }
        return this;
    }

    /**************************
     * Sequence related method
     **************************/

    public ChainTourGuide playInSequence(Sequence sequence){
        setSequence(sequence);
        next();
        return this;
    }

    public ChainTourGuide setSequence(Sequence sequence){
        mSequence = sequence;
        mSequence.setParentTourGuide(this);
        for (ChainTourGuide tourGuide : sequence.mTourGuideArray){
            if (tourGuide.mHighlightedView == null) {
                throw new NullPointerException("Please specify the view using 'playLater' method");
            }
        }
        return this;
    }
}
