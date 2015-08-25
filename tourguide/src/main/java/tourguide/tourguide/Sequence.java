package tourguide.tourguide;

import android.view.View;

/**
 * Created by aaronliew on 8/7/15.
 */
public class Sequence {
    TourGuide [] mTourGuideArray;
    Overlay mDefaultOverlay;
    ToolTip mDefaultToolTip;
    Pointer mDefaultPointer;

    ContinueMethod mContinueMethod;
    boolean mDisableTargetButton;
    public int mCurrentSequence;
    TourGuide mParentTourGuide;
    public enum ContinueMethod {
        Overlay, OverlayListener
    }
    private Sequence(SequenceBuilder builder){
        this.mTourGuideArray = builder.mTourGuideArray;
        this.mDefaultOverlay = builder.mDefaultOverlay;
        this.mDefaultToolTip = builder.mDefaultToolTip;
        this.mDefaultPointer = builder.mDefaultPointer;
        this.mContinueMethod = builder.mContinueMethod;
        this.mCurrentSequence = builder.mCurrentSequence;

        // TODO: to be implemented
        this.mDisableTargetButton = builder.mDisableTargetButton;
    }

    /**
     * sets the parent TourGuide that will run this Sequence
     */
    protected void setParentTourGuide(TourGuide parentTourGuide){
        mParentTourGuide = parentTourGuide;

        if(mContinueMethod == ContinueMethod.Overlay) {
            for (final TourGuide tourGuide : mTourGuideArray) {
                tourGuide.mOverlay.mOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mParentTourGuide.next();
                    }
                };
            }
        }
    }

    public TourGuide getNextTourGuide() {
        return mTourGuideArray[mCurrentSequence];
    }

    public ContinueMethod getContinueMethod() {
        return mContinueMethod;
    }

    public TourGuide[] getTourGuideArray() {
        return mTourGuideArray;
    }

    public Overlay getDefaultOverlay() {
        return mDefaultOverlay;
    }

    public ToolTip getDefaultToolTip() {
        return mDefaultToolTip;
    }

    public ToolTip getToolTip() {
        // individual tour guide has higher priority
        if (mTourGuideArray[mCurrentSequence].mToolTip != null){
            return mTourGuideArray[mCurrentSequence].mToolTip;
        } else {
            return mDefaultToolTip;
        }
    }

    public Overlay getOverlay() {
        // Overlay is used as a method to proceed to next TourGuide, so the default overlay is already assigned appropriately if needed
        return mTourGuideArray[mCurrentSequence].mOverlay;
    }

    public Pointer getPointer() {
        // individual tour guide has higher priority
        if (mTourGuideArray[mCurrentSequence].mPointer != null){
            return mTourGuideArray[mCurrentSequence].mPointer;
        } else {
            return mDefaultPointer;
        }
    }

    public static class SequenceBuilder {
        TourGuide [] mTourGuideArray;
        Overlay mDefaultOverlay;
        ToolTip mDefaultToolTip;
        Pointer mDefaultPointer;
        ContinueMethod mContinueMethod;
        int mCurrentSequence;
        boolean mDisableTargetButton;

        public SequenceBuilder add(TourGuide... tourGuideArray){
            mTourGuideArray = tourGuideArray;
            return this;
        }

        public SequenceBuilder setDefaultOverlay(Overlay defaultOverlay){
            mDefaultOverlay = defaultOverlay;
            return this;
        }

        // This might not be useful, but who knows.. maybe someone needs it
        public SequenceBuilder setDefaultToolTip(ToolTip defaultToolTip){
            mDefaultToolTip = defaultToolTip;
            return this;
        }

        public SequenceBuilder setDefaultPointer(Pointer defaultPointer){
            mDefaultPointer = defaultPointer;
            return this;
        }

        // TODO: this is an uncompleted feature, make it private first
        // This is intended to be used to disable the button, so people cannot click on in during a Tour, instead, people can only click on Next button or Overlay to proceed
        private SequenceBuilder setDisableButton(boolean disableTargetButton){
            mDisableTargetButton = disableTargetButton;
            return this;
        }

        /**
         * @param continueMethod ContinueMethod.Overlay or ContinueMethod.OverlayListener
         *                       ContnueMethod.Overlay - clicking on Overlay will make TourGuide proceed to the next one.
         *                       ContinueMethod.OverlayListener - you need to provide OverlayListeners, and call tourGuideHandler.next() in the listener to proceed to the next one.
         */
        public SequenceBuilder setContinueMethod(ContinueMethod continueMethod){
            mContinueMethod = continueMethod;
            return this;
        }

        public Sequence build(){
            mCurrentSequence = 0;
            checkIfContinueMethodNull();
            checkAtLeastTwoTourGuideSupplied();
            checkOverlayListener(mContinueMethod);

            return new Sequence(this);
        }
        private void checkIfContinueMethodNull(){
            if (mContinueMethod == null){
                throw new IllegalArgumentException("Continue Method is not set. Please provide ContinueMethod in setContinueMethod");
            }
        }
        private void checkAtLeastTwoTourGuideSupplied() {
            if (mTourGuideArray == null || mTourGuideArray.length <= 1){
                throw new IllegalArgumentException("In order to run a sequence, you must at least supply 2 TourGuide into Sequence using add()");
            }
        }
        private void checkOverlayListener(ContinueMethod continueMethod) {
            if(continueMethod == ContinueMethod.OverlayListener){
                boolean pass = true;
                if (mDefaultOverlay != null && mDefaultOverlay.mOnClickListener != null) {
                    pass = true;
                    // when default listener is available, we loop through individual tour guide, and
                    // assign default listener to individual tour guide
                    for (TourGuide tourGuide : mTourGuideArray) {
                        if (tourGuide.mOverlay == null) {
                            tourGuide.mOverlay = mDefaultOverlay;
                        }
                        if (tourGuide.mOverlay != null && tourGuide.mOverlay.mOnClickListener == null) {
                            tourGuide.mOverlay.mOnClickListener = mDefaultOverlay.mOnClickListener;
                        }
                    }
                } else { // case where: default listener is not available

                    for (TourGuide tourGuide : mTourGuideArray) {
                        //Both of the overlay and default listener is not null, throw the error
                        if (tourGuide.mOverlay != null && tourGuide.mOverlay.mOnClickListener == null) {
                            pass = false;
                            break;
                        } else if (tourGuide.mOverlay == null){
                            pass = false;
                            break;
                        }
                    }

                }

                if (!pass){
                    throw new IllegalArgumentException("ContinueMethod.OverlayListener is chosen as the ContinueMethod, but no Default Overlay Listener is set, or not all Overlay.mListener is set for all the TourGuide passed in.");
                }
            } else if(continueMethod == ContinueMethod.Overlay){
                // when Overlay ContinueMethod is used, listener must not be supplied (to avoid unexpected result)
                boolean pass = true;
                if (mDefaultOverlay != null && mDefaultOverlay.mOnClickListener != null) {
                    pass = false;
                } else {
                    for (TourGuide tourGuide : mTourGuideArray) {
                        if (tourGuide.mOverlay != null && tourGuide.mOverlay.mOnClickListener != null ) {
                            pass = false;
                            break;
                        }
                    }
                }
                if (mDefaultOverlay != null) {
                    for (TourGuide tourGuide : mTourGuideArray) {
                        if (tourGuide.mOverlay == null) {
                            tourGuide.mOverlay = mDefaultOverlay;
                        }
                    }
                }

                if (!pass) {
                    throw new IllegalArgumentException("ContinueMethod.Overlay is chosen as the ContinueMethod, but either default overlay listener is still set OR individual overlay listener is still set, make sure to clear all Overlay listener");
                }
            }
        }
    }
}