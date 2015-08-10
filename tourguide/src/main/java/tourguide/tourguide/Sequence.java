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

    public static int ContinueMethodOverlay = 1;
    public static int ContinueMethodToolTip = 2;

    private Sequence(SequenceBuilder builder){
        //TODO
        this.mTourGuideArray = builder.mTourGuideArray;
        this.mDefaultOverlay = builder.mDefaultOverlay;
        this.mDefaultToolTip = builder.mDefaultToolTip;
        this.mDefaultPointer = builder.mDefaultPointer;
        this.mContinueMethod = builder.mContinueMethod;
        this.mDisableTargetButton = builder.mDisableTargetButton;
    }

    public static class SequenceBuilder {
        TourGuide [] mTourGuideArray;
        Overlay mDefaultOverlay;
        ToolTip mDefaultToolTip;
        Pointer mDefaultPointer;
        int mContinueMethod;
        boolean mDisableTargetButton;

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
            mDefaultPointer=defaultPointer;
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
            // TODO: check that it must be one of: Overlay, ToolTip or both
            // TODO: implement

            boolean IsOverlayClickListenerNull=true;
            boolean IsTooltipClickListenerNull=true;

            for (TourGuide tourGuide : mTourGuideArray){
                if (tourGuide.mOverlay!=null && tourGuide.mOverlay.mOnClickListener!=null){
                    IsOverlayClickListenerNull=false;
                }

                if (tourGuide.mToolTip!=null && tourGuide.mToolTip.mOnClickListener!=null){
                    IsTooltipClickListenerNull=false;
                }
            }

            if (continueMethod==ContinueMethod.Overlay){
                mContinueMethod = ContinueMethod.Overlay;
                Log.d("tourguide", "Continue Method: Overlay");
                CheckOverlayListenerConflict(IsOverlayClickListenerNull);
            }

            else if (continueMethod ==ContinueMethod.ToolTip){
                mContinueMethod = ContinueMethod.ToolTip;
                Log.d("tourguide", "Continue Method: ToolTip");
                CheckToolTipListenerConflict(IsTooltipClickListenerNull);
            }

            else{
                mContinueMethod = ContinueMethod.Overlay | ContinueMethod.ToolTip;
                Log.d("tourguide", "Continue Method: Overlay|ToolTip");
                CheckToolTipListenerConflict(IsTooltipClickListenerNull);
                CheckOverlayListenerConflict(IsOverlayClickListenerNull);
            }


            return this;
        }

        public Sequence build(){
            // TODO: check that it must have mTourGuideArray with at least 2 items, and choose a ContinueMethod, else an throw error with proper help message
            return new Sequence(this);
        }

        private void CheckToolTipListenerConflict(boolean IsTooltipClickListenerNull){
            if (!IsTooltipClickListenerNull && mDefaultToolTip!=null && mDefaultToolTip.mOnClickListener!=null){
                throw new RuntimeException("Default ToolTip OnClickListener is not null. Please remove it to avoid conflict with other ToolTip's onClickListener");
            }
        }

        private void CheckOverlayListenerConflict(boolean IsOverlayClickListenerNull){
            if (!IsOverlayClickListenerNull && mDefaultOverlay!=null && mDefaultOverlay.mOnClickListener!=null){
                throw new RuntimeException("Default Overlay OnClickListener is not null. Please remove it to avoid conflict with other Overlay's onClickListener");
            }
        }
    }
}