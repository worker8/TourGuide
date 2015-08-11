package tourguide.tourguide;

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

    public static class SequenceBuilder {
        TourGuide [] mTourGuideArray;
        Overlay mDefaultOverlay;
        ToolTip mDefaultToolTip;
        Pointer mDefaultPointer;
        int mContinueMethod;
        boolean mDisableTargetButton;
        int mCurrentSequence;

        public SequenceBuilder add(TourGuide... tourGuideArray){
            mTourGuideArray = tourGuideArray;
            return this;
        }

        // TODO: implement
        public SequenceBuilder setDefaultOverlay(Overlay defaultOverlay){
            mDefaultOverlay = defaultOverlay;
            return this;
        }

        // This might not be useful, but who knows.. maybe someone needs it
        // TODO: implement
        public SequenceBuilder setDefaultToolTip(ToolTip defaultToolTip){
            mDefaultToolTip=defaultToolTip;
            return this;
        }

        // TODO: implement
        public SequenceBuilder setDefaultPointer(Pointer defaultPointer){
            mDefaultPointer = defaultPointer;
            return this;
        }

        // TODO: implement
        public SequenceBuilder setDisableButton(boolean disableTargetButton){
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

            checkAtLeastTwoTourGuideSupplied();
            checkListener(mContinueMethod);

            return new Sequence(this);
        }

        private void checkListener(int continueMethod) {
            if( ((continueMethod & ContinueMethod.OverlayListener) == ContinueMethod.OverlayListener)
                    && ((continueMethod & ContinueMethod.Overlay) == ContinueMethod.Overlay)){
                // check: either Overlay or OverlayListener can be used, not both at the same time
                throw new IllegalArgumentException("Sequence's continueMethod is set to ContinueMethod.Overlay | ContinueMethod.OverlayListener, this is ambiguous and TourGuide cannot tell if you want to go next by clicking Overlay or by using your custom Overlay Listener. Please fix by removing either one");
            } else if ( ((continueMethod & ContinueMethod.ToolTipListener) == ContinueMethod.ToolTipListener)
                    && ((continueMethod & ContinueMethod.ToolTip) == ContinueMethod.ToolTip)){
                // check: either Overlay or OverlayListener can be used, not both at the same time
                throw new IllegalArgumentException("Sequence's continueMethod is set to ContinueMethod.ToolTip | ContinueMethod.ToolTipListener, this is ambiguous and TourGuide cannot tell if you want to go next by clicking ToolTip or by using your custom ToolTip Listener. Please fix by removing either one");
            } else if ((continueMethod & ContinueMethod.Overlay) == ContinueMethod.Overlay) {

            } else if ((continueMethod & ContinueMethod.OverlayListener) == ContinueMethod.OverlayListener) {

                boolean pass = false;
                if (mDefaultOverlay != null && mDefaultOverlay.mOnClickListener != null) {
                    pass = true;
                } else {
                    pass = true;
                    for (TourGuide tourGuide : mTourGuideArray){
                        if (tourGuide.mOverlay != null && tourGuide.mOverlay.mOnClickListener != null){
                            continue;
                        } else {
                            pass = false;
                            break;
                        }
                    }
                }
                if (!pass){
                    throw new IllegalArgumentException("ContinueMethod.OverlayListener is chosen as the ContinueMethod, but no Default Overlay Listener is set, and not all Overlay.mLisnter is set for all the TourGuide passed in.");
                }
            } else if ((continueMethod & ContinueMethod.ToolTip) == ContinueMethod.ToolTip) {

            } else if ((continueMethod & ContinueMethod.ToolTipListener) == ContinueMethod.ToolTipListener) {
                boolean pass = false;
                if (mDefaultToolTip != null && mDefaultToolTip.mOnClickListener != null) {
                    pass = true;
                } else {
                    pass = true;
                    for (TourGuide tourGuide : mTourGuideArray){
                        if (tourGuide.mToolTip != null && tourGuide.mToolTip.mOnClickListener != null){
                            continue;
                        } else {
                            pass = false;
                            break;
                        }
                    }
                }
                if (!pass){
                    throw new IllegalArgumentException("ContinueMethod.OverlayListener is chosen as the ContinueMethod, but no Default Overlay Listener is set, and not all Overlay.mLisnter is set for all the TourGuide passed in.");
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