package tourguide.tourguide;

import android.util.Log;
import android.view.View;

/**
 * Created by aaronliew on 8/7/15.
 */
public class Sequence {
    TourGuide [] mTourGuideArray;
    Overlay mDefaultOverlay;
    ToolTip mDefaultToolTip;
    Pointer mDefaultPointer;

    View.OnClickListener mOverlayListener, mToolTipListener;
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
        //Individual tourGuide is always has the highest priority
        if (mTourGuideArray[mCurrentSequence].mToolTip!=null && mTourGuideArray[mCurrentSequence].mToolTip.mOnClickListener!=null){
            mToolTipListener = mTourGuideArray[mCurrentSequence].mToolTip.mOnClickListener;
        }
        else if (mDefaultToolTip!=null && mDefaultToolTip.mOnClickListener!=null){
            mToolTipListener = mDefaultToolTip.mOnClickListener;
        }

        if (mTourGuideArray[mCurrentSequence].mToolTip!=null){
            return mTourGuideArray[mCurrentSequence].mToolTip;
        }

        else {
            return mDefaultToolTip;
        }
    }

    public View.OnClickListener getToolTipListener() {
        return mToolTipListener;
    }

    public View.OnClickListener getOverlayListener() {
        return mOverlayListener;
    }

    public Overlay getOverlay() {
        //Individual tourGuide is always has the highest priority. Check if the listener is null then set it.
        if (mTourGuideArray[mCurrentSequence].mOverlay!=null && mTourGuideArray[mCurrentSequence].mOverlay.mOnClickListener!=null){
            mOverlayListener = mTourGuideArray[mCurrentSequence].mOverlay.mOnClickListener;
        }
        else if (mDefaultOverlay!=null && mDefaultOverlay.mOnClickListener!=null) {
            mOverlayListener = mDefaultOverlay.mOnClickListener;
        }

        //Individual tourGuide is always has the highest priority
        if (mTourGuideArray[mCurrentSequence].mOverlay!=null){
            return mTourGuideArray[mCurrentSequence].mOverlay;
        }

        else {
            return mDefaultOverlay;
        }
    }

    public Pointer getPointer() {
        //Individual tourGuide is always has the highest priority
        if (mTourGuideArray[mCurrentSequence].mPointer!=null){
            return mTourGuideArray[mCurrentSequence].mPointer;
        }

        else {
            return mDefaultPointer;
        }
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
            if( ((continueMethod & ContinueMethod.OverlayListener) == ContinueMethod.OverlayListener)
                    && ((continueMethod & ContinueMethod.Overlay) == ContinueMethod.Overlay)) {
                // check: either Overlay or OverlayListener can be used, not both at the same time
                throw new IllegalArgumentException("Sequence's continueMethod is set to ContinueMethod.Overlay | ContinueMethod.OverlayListener, this is ambiguous and TourGuide cannot tell if you want to go next by clicking Overlay or by using your custom Overlay Listener. Please fix by removing either one");
            }

            if ( ((continueMethod & ContinueMethod.ToolTipListener) == ContinueMethod.ToolTipListener)
                    && ((continueMethod & ContinueMethod.ToolTip) == ContinueMethod.ToolTip)){
                // check: either Overlay or OverlayListener can be used, not both at the same time
                throw new IllegalArgumentException("Sequence's continueMethod is set to ContinueMethod.ToolTip | ContinueMethod.ToolTipListener, this is ambiguous and TourGuide cannot tell if you want to go next by clicking ToolTip or by using your custom ToolTip Listener. Please fix by removing either one");
            }

//            if ((continueMethod & ContinueMethod.Overlay) == ContinueMethod.Overlay) {
//
//            } else

            if ((continueMethod & ContinueMethod.OverlayListener) == ContinueMethod.OverlayListener) {

                boolean pass = true;

                if (mDefaultOverlay != null && mDefaultOverlay.mOnClickListener != null) {
                    pass = true;
                }
                else{

                    for (TourGuide tourGuide : mTourGuideArray) {
                        //Both of the overlay and default listener is not null, throw the error
                        if (tourGuide.mOverlay!=null && tourGuide.mOverlay.mOnClickListener==null) {
                            pass = false;
                            break;
                        }
                        else if (tourGuide.mOverlay==null){
                            pass = false;
                            break;
                        }
                    }

                }

                if (!pass){
                    throw new IllegalArgumentException("ContinueMethod.OverlayListener is chosen as the ContinueMethod, but no Default Overlay Listener is set, and not all Overlay.mListener is set for all the TourGuide passed in.");
                }
            }

//            if ((continueMethod & ContinueMethod.ToolTip) == ContinueMethod.ToolTip) {
//
//            } else

            if ((continueMethod & ContinueMethod.ToolTipListener) == ContinueMethod.ToolTipListener) {
                boolean pass = true;

                if (mDefaultToolTip != null && mDefaultToolTip.mOnClickListener != null) {
                    pass = true;
                }

                for (TourGuide tourGuide : mTourGuideArray){
                    //Both of the tooltip and default listener is not null, throw the error
                    if (tourGuide.mToolTip!=null && tourGuide.mToolTip.mOnClickListener==null) {
                        pass = false;
                        break;
                    }
                    else if (tourGuide.mToolTip==null){
                        pass = false;
                        break;
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