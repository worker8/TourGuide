package tourguide.tourguide;

import android.util.Log;

/**
 * Created by aaronliew on 8/7/15.
 */
public class Sequence {
    TourGuide [] mTourGuideArray;
    Overlay mDefaultOverlay;
    ToolTip mDefaultToolTip;
    Pointer mDefaultPointer;

    int mContinueMethod;
    boolean mDisableTargetButton;
    public int mCurrentSequence;

    private Sequence(SequenceBuilder builder){
        //TODO
        this.mTourGuideArray = builder.mTourGuideArray;
        this.mDefaultOverlay = builder.mDefaultOverlay;
        this.mDefaultToolTip = builder.mDefaultToolTip;
        this.mDefaultPointer = builder.mDefaultPointer;
        this.mContinueMethod = builder.mContinueMethod;
        this.mDisableTargetButton = builder.mDisableTargetButton;
        this.mCurrentSequence = builder.mCurrentSequence;
    }

    public TourGuide getNextTourGuide() {
        return mTourGuideArray[mCurrentSequence];
    }

    public int getContinueMethod() {
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
        // individual tour guide has higher priority
        if (mTourGuideArray[mCurrentSequence].mOverlay != null){
            return mTourGuideArray[mCurrentSequence].mOverlay;
        } else {
            return mDefaultOverlay;
        }
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
        int mContinueMethod;
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
            mDefaultToolTip=defaultToolTip;
            return this;
        }

        public SequenceBuilder setDefaultPointer(Pointer defaultPointer){
            mDefaultPointer = defaultPointer;
            return this;
        }

        // TODO: this is an uncompleted feature, make it private first
        // This is intended to be used to disable the button, so people cannot click on in during a Tour, instead, people can only click on Next button or Overlay/ToolTip to proceed
        private SequenceBuilder setDisableButton(boolean disableTargetButton){
            mDisableTargetButton = disableTargetButton;
            return this;
        }

        /**
         * @param continueMethod should only be Overlay or ToolTip, if both are intended to be clickable, user can use Overlay|ToolTip
         */
        public SequenceBuilder setContinueMethod(int continueMethod){
            mContinueMethod = continueMethod;
            return this;
        }

        public Sequence build(){
            mCurrentSequence = 0;
            checkIfContinueMethodNull();
            checkAtLeastTwoTourGuideSupplied();
            checkListener(mContinueMethod);
            return new Sequence(this);
        }

        private void checkIfContinueMethodNull(){
            if (mContinueMethod==0){
                throw new IllegalArgumentException("Continue Method is not set. Please provide ContinueMethod in setContinueMethod");
            }
        }

        private void checkListener(int continueMethod) {
            Log.d("Check listener",String.valueOf(continueMethod));
            // Only 1 Overlay Continue Method Check: either one of Overlay or OverlayListener can be used, not both at the same time
            if( ((continueMethod & ContinueMethod.OverlayListener) == ContinueMethod.OverlayListener)
                    && ((continueMethod & ContinueMethod.Overlay) == ContinueMethod.Overlay)) {
                throw new IllegalArgumentException("Sequence's continueMethod is set to ContinueMethod.Overlay | ContinueMethod.OverlayListener, this is ambiguous and TourGuide cannot tell if you want to go next by clicking Overlay or by using your custom Overlay Listener. Please fix by removing either one");
            }

            // Only 1 ToolTip Continue Method Check: either one of ToolTip or ToolTipListener can be used, not both at the same time
            if ( ((continueMethod & ContinueMethod.ToolTipListener) == ContinueMethod.ToolTipListener)
                    && ((continueMethod & ContinueMethod.ToolTip) == ContinueMethod.ToolTip)){
                throw new IllegalArgumentException("Sequence's continueMethod is set to ContinueMethod.ToolTip | ContinueMethod.ToolTipListener, this is ambiguous and TourGuide cannot tell if you want to go next by clicking ToolTip or by using your custom ToolTip Listener. Please fix by removing either one");
            }
            // Listener must not be supplied (to avoid unexpected result), when Overlay ContinueMethod is used
            if ((continueMethod & ContinueMethod.Overlay) == ContinueMethod.Overlay) {
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
                if (!pass) {
                    throw new IllegalArgumentException("ContinueMethod.Overlay is chosen as the ContinueMethod, but either default overlay listener is still set OR individual overlay listener is still set, make sure to clear all Overlay listener");
                }
            }
            // Listener must not be supplied (to avoid unexpected result), when ToolTip ContinueMethod is used
            if ((continueMethod & ContinueMethod.ToolTip) == ContinueMethod.ToolTip) {
                boolean pass = true;
                if (mDefaultToolTip != null && mDefaultToolTip.mOnClickListener != null) {
                    pass = false;
                } else {
                    for (TourGuide tourGuide : mTourGuideArray) {
                        if (tourGuide.mToolTip != null) {
                            pass = false;
                            break;
                        }
                    }
                }
                if (!pass) {
                    throw new IllegalArgumentException("ContinueMethod.ToolTip is chosen as the ContinueMethod, but either default ToolTip listener is still set OR individual ToolTip listener is still set, make sure to clear all ToolTip listener");
                }
            }

            // Listener must be supplied when OverlayListener ContinueMethod is used
            if ((continueMethod & ContinueMethod.OverlayListener) == ContinueMethod.OverlayListener) {

                boolean pass = true;

                if (mDefaultOverlay != null && mDefaultOverlay.mOnClickListener != null) {
                    pass = true;

                    // when default listener is available, we loop through individual tour guide, and
                    // assign default listener to individual tour guide
                    for (TourGuide tourGuide : mTourGuideArray) {
                        if (tourGuide.mOverlay != null && tourGuide.mOverlay.mOnClickListener == null) {
                            tourGuide.mOverlay.mOnClickListener = mDefaultOverlay.mOnClickListener;
                        }
                    }
                } else { // case where: default listener is not available

                    for (TourGuide tourGuide : mTourGuideArray) {
                        //Both of the overlay and default listener is not null, throw the error
                        if (tourGuide.mOverlay!=null && tourGuide.mOverlay.mOnClickListener==null) {
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
            }

            // Listener must be supplied when ToolTipListener ContinueMethod is used
            if ((continueMethod & ContinueMethod.ToolTipListener) == ContinueMethod.ToolTipListener) {
                boolean pass = true;

                if (mDefaultToolTip != null && mDefaultToolTip.mOnClickListener != null) {
                    pass = true;
                    // assign default listener if individual tooltip doesn't exist
                    for (TourGuide tourGuide : mTourGuideArray) {
                        //Both of the overlay and default listener is not null, throw the error
                        if (tourGuide.mToolTip != null && tourGuide.mToolTip.mOnClickListener == null) {
                            tourGuide.mToolTip.mOnClickListener = mDefaultToolTip.mOnClickListener;
                        }
                    }
                } else {

                    for (TourGuide tourGuide : mTourGuideArray){
                        //Both of the tooltip and default listener is not null, throw the error
                        if (tourGuide.mToolTip != null && tourGuide.mToolTip.mOnClickListener == null) {
                            pass = false;
                            break;
                        } else if (tourGuide.mToolTip == null){
                            pass = false;
                            break;
                        }

                    }
                }
                if (!pass){
                    throw new IllegalArgumentException("ContinueMethod.ToolTipListener is chosen as the ContinueMethod, but no Default Tooltip Listener is set, and not all Tooltip.mListener is set for all the TourGuide passed in.");
                }
            }
        }

        private void checkAtLeastTwoTourGuideSupplied() {
            if (mTourGuideArray == null || mTourGuideArray.length <= 1){
                throw new IllegalArgumentException("In order to run a sequence, you must at least supply 2 TourGuide into Sequence using add()");
            }
        }
    }
}