package tourguide.tourguide;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;


/**
 * Created by tanjunrong on 2/10/15.
 */
public class TourGuide {
    /**
     * This describes the animation techniques
     * */
    public enum Technique {
        Click, HorizontalLeft, HorizontalRight, VerticalUpward, VerticalDownward
    }

    /**
     * This describes the allowable motion, for example if you want the users to learn about clicking, but want to stop them from swiping, then use ClickOnly
     */
    public enum MotionType {
        AllowAll, ClickOnly, SwipeOnly
    }
    private Technique mTechnique;
    private int mDuration;
    private View mHighlightedView;
    private int mOverlayBackgroundColor = Color.TRANSPARENT;
    private boolean mDisableClick = false;
    private Activity mActivity;
    private MotionType mMotionType;
    private int mGravity = Gravity.CENTER;
    private FrameLayoutWithHole mFrameLayout;
    private View mDescriptionViewGroup;
    private ToolTip mToolTip;
    /* Static builder */
    public static TourGuide init(Activity activity){
        return new TourGuide(activity);
    }

    /* Constructor */
    public TourGuide(Activity activity){
        mActivity = activity;
    }

    /**
     * Setter for the animation to be used
     * @param technique Animation to be used
     * @return return AnimateTutorial instance for chaining purpose
     */
    public TourGuide with(Technique technique){
        mTechnique = technique;
        return this;
    }

    /**
     * Sets which motion type is motionType
     * @param motionType
     * @return return AnimateTutorial instance for chaining purpose
     */
    public TourGuide motionType(MotionType motionType){
        mMotionType = motionType;
        return this;
    }

    /**
     * Sets the duration
     * @param duration duration for the animation to happen, in miliseconds
     * @return return AnimateTutorial instance for chaining purpose
     */
    public TourGuide duration(int duration){
        mDuration = duration;
        return this;
    }

    /**
     * Sets the duration
     * @param view the view in which the tutorial button will be placed on top of
     * @return return AnimateTutorial instance for chaining purpose
     */
    public TourGuide playOn(View view){
        mHighlightedView = view;
        setupView();
        return this;
    }
    /**
     * Sets the gravity
     * @param gravity Gravity.CENTER for example, the
     * @return return AnimateTutorial instance for chaining purpose
     */
    public TourGuide gravity(int gravity){
        mGravity = gravity;
        return this;
    }
    /**
     * Sets the background color for the overlay
     * @param color the color of the background, default to transparent
     * @return return AnimateTutorial instance for chaining purpose
     */
    public TourGuide overlayColor(int color){
        mOverlayBackgroundColor = color;
        return this;
    }

    /**
     * Setting true will disable the clicking on any view behind the target view in tutorial
     * @param disable true to disable clicking, false to enable clicking, default to false
     * @return return AnimateTutorial instance for chaining purpose
     */
    public TourGuide disableClick(boolean disable){
        mDisableClick = disable;
        return this;
    }
    /**
     * Set the toolTip
     * @param toolTip
     * @return return AnimateTutorial instance for chaining purpose
     */
    public TourGuide toolTip(ToolTip toolTip){
        mToolTip = toolTip;
        return this;
    }
    /**
     * Clean up the tutorial that is added to the activity
     */
    public void cleanUp(){
        if (mFrameLayout.getParent()!=null){
            ((ViewGroup)mFrameLayout.getParent()).removeView(mFrameLayout);
            ((ViewGroup) mActivity.getWindow().getDecorView()).removeView(mDescriptionViewGroup);
        }
    }
    private int getXForToolTip(int width){
        int [] pos = new int[2];
        mHighlightedView.getLocationOnScreen(pos);
        int x = pos[0];
        return x+mHighlightedView.getWidth()/2-width/2;
    }
    private int getYForToolTip(int height){
        int [] pos = new int[2];
        mHighlightedView.getLocationOnScreen(pos);
        int y = pos[1];
        return y+mHighlightedView.getHeight();
    }
    private int getXBasedOnGravity(int width){
        int [] pos = new int[2];
        mHighlightedView.getLocationOnScreen(pos);
        int x = pos[0];
        if((mGravity & Gravity.RIGHT) == Gravity.RIGHT){
            return x+mHighlightedView.getWidth()-width;
        } else if ((mGravity & Gravity.LEFT) == Gravity.LEFT) {
            return x;
         }else { // this is center
            return x+mHighlightedView.getWidth()/2-width/2;
        }
    }
    private int getYBasedOnGravity(int height){
        int [] pos = new int[2];
        mHighlightedView.getLocationInWindow(pos);
        int y = pos[1];
        Log.d("ddw-l","fab height: "+height);
        Log.d("ddw-l","mHighlightedView height: "+mHighlightedView.getHeight());
        Log.d("ddw-l","mHighlightedView.getLocationInWindow(): "+y);
        Log.d("ddw-l","mHighlightedView.getY(): "+mHighlightedView.getY());
        if((mGravity & Gravity.BOTTOM) == Gravity.BOTTOM){
            return y+mHighlightedView.getHeight()-height;
        } else if ((mGravity & Gravity.TOP) == Gravity.TOP) {
            return y;
        }else { // this is center
            return y+mHighlightedView.getHeight()/2-height/2;
        }
    }
//    final int description_enter_animation_duration = 1000;
    private void setupView(){
//        TODO: throw exception if either mActivity, mDuration, mHighlightedView is null
        final ViewTreeObserver viewTreeObserver = mHighlightedView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // make sure this only run once
                mHighlightedView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.d("ddw", "HighlightedView.getHeight(): " + mHighlightedView.getHeight());
                Log.d("ddw", "HighlightedView.getWidth(): " + mHighlightedView.getWidth());

                /* Initialize a frame layout with a hole */
                mFrameLayout = new FrameLayoutWithHole(mActivity, mMotionType);
                mFrameLayout.setBackgroundColor(mOverlayBackgroundColor);

                /* handle click disable */
                handleDisableClicking(mFrameLayout);

                /* setup tooltip view */
                setupToolTip(mFrameLayout);

                /* setup floating action button */
                FloatingActionButton fab = setupAndAddFABToFrameLayout(mFrameLayout);

                performAnimationOn(fab);
            }
        });
    }
    private void handleDisableClicking(FrameLayoutWithHole frameLayoutWithHole){
        if (mDisableClick) {
            frameLayoutWithHole.setViewHole(mHighlightedView);
            frameLayoutWithHole.setSoundEffectsEnabled(false);
            frameLayoutWithHole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("ddw", "disable, do nothing");
                }
            });
        }
    }
    private void setupToolTip(FrameLayoutWithHole frameLayoutWithHole){
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.gravity = Gravity.BOTTOM;

        if (mToolTip != null) {
            LayoutInflater layoutInflater = mActivity.getLayoutInflater();
            mDescriptionViewGroup = layoutInflater.inflate(R.layout.tooltip, null);
            View container = mDescriptionViewGroup.findViewById(R.id.toolTip_container);
            TextView titleTV = (TextView)mDescriptionViewGroup.findViewById(R.id.title);
            TextView mDescriptionTV = (TextView)mDescriptionViewGroup.findViewById(R.id.description);
            titleTV.setText(mToolTip.mTitle);
            mDescriptionTV.setText(mToolTip.mDescription);
            container.setBackgroundColor(mToolTip.mBackgroundColor);

            mDescriptionViewGroup.startAnimation(mToolTip.mEnterAnimation);

            // measure size of image to be placed
            mDescriptionViewGroup.measure(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            int width = mDescriptionViewGroup.getMeasuredWidth();
            int height = mDescriptionViewGroup.getMeasuredHeight();

            layoutParams.setMargins(getXForToolTip(width), getYForToolTip(height), 0, 0);
            /* add shadow if it's turned on */
            if (mToolTip.mShadow) {
                mDescriptionViewGroup.setBackgroundDrawable(mActivity.getResources().getDrawable(android.R.drawable.dialog_holo_light_frame));
            }
//            ((ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content)).addView(mDescriptionViewGroup, layoutParams);
            ((ViewGroup) mActivity.getWindow().getDecorView()).addView(mDescriptionViewGroup, layoutParams);
        }

    }
    private FloatingActionButton setupAndAddFABToFrameLayout(FrameLayoutWithHole frameLayoutWithHole){
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        FloatingActionButton fab = new FloatingActionButton(mActivity);
        fab.setColorNormal(mActivity.getResources().getColor(R.color.White));
        fab.setClickable(false);
        // measure size of image to be placed
        fab.measure(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        int width = fab.getMeasuredWidth();
        int height = fab.getMeasuredHeight();

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(getXBasedOnGravity(width), getYBasedOnGravity(height), 0, 0);
        frameLayoutWithHole.addView(fab, params);

        // getDecorView() is used without .findViewById(android.R.id.content) because we want the absolute coordinates, not just the content area ones
        ((ViewGroup) mActivity.getWindow().getDecorView()).addView(frameLayoutWithHole, layoutParams);
        return fab;
    }

    private void performAnimationOn(final View view){
        AnimationSet animSet = new AnimationSet(true);

        if (mTechnique != null && mTechnique == Technique.HorizontalLeft){

            final AnimatorSet animatorSet = new AnimatorSet();
            final AnimatorSet animatorSet2 = new AnimatorSet();
            Animator.AnimatorListener lis1 = new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animator) {}
                @Override public void onAnimationCancel(Animator animator) {}
                @Override public void onAnimationRepeat(Animator animator) {}
                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                    view.setTranslationX(0);
                    animatorSet2.start();
                }
            };
            Animator.AnimatorListener lis2 = new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animator) {}
                @Override public void onAnimationCancel(Animator animator) {}
                @Override public void onAnimationRepeat(Animator animator) {}
                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                    view.setTranslationX(0);
                    animatorSet.start();
                }
            };

            long fadeInDuration = 800;
            long scaleDownDuration = 800;
            long goLeftXDuration = 2000;
            long fadeOutDuration = goLeftXDuration;
            float translationX = getScreenWidth()/2;

            final ValueAnimator fadeInAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeInAnim.setDuration(fadeInDuration);
            final ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f);
            scaleDownX.setDuration(scaleDownDuration);
            final ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f);
            scaleDownY.setDuration(scaleDownDuration);
            final ObjectAnimator goLeftX = ObjectAnimator.ofFloat(view, "translationX", -translationX);
            goLeftX.setDuration(goLeftXDuration);
            final ValueAnimator fadeOutAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            fadeOutAnim.setDuration(fadeOutDuration);

            final ValueAnimator fadeInAnim2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeInAnim2.setDuration(fadeInDuration);
            final ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f);
            scaleDownX2.setDuration(scaleDownDuration);
            final ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f);
            scaleDownY2.setDuration(scaleDownDuration);
            final ObjectAnimator goLeftX2 = ObjectAnimator.ofFloat(view, "translationX", -translationX);
            goLeftX2.setDuration(goLeftXDuration);
            final ValueAnimator fadeOutAnim2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            fadeOutAnim2.setDuration(fadeOutDuration);

            animatorSet.play(fadeInAnim);
            animatorSet.play(scaleDownX).with(scaleDownY).after(fadeInAnim);
            animatorSet.play(goLeftX).with(fadeOutAnim).after(scaleDownY);

            animatorSet2.play(fadeInAnim2);
            animatorSet2.play(scaleDownX2).with(scaleDownY2).after(fadeInAnim2);
            animatorSet2.play(goLeftX2).with(fadeOutAnim2).after(scaleDownY2);

            animatorSet.addListener(lis1);
            animatorSet2.addListener(lis2);
            animatorSet.start();

        } else if (mTechnique != null && mTechnique == Technique.HorizontalRight){

        } else if (mTechnique != null && mTechnique == Technique.VerticalUpward){

        } else if (mTechnique != null && mTechnique == Technique.VerticalDownward){

        } else { // do click for default case
            final AnimatorSet animatorSet = new AnimatorSet();
            final AnimatorSet animatorSet2 = new AnimatorSet();
            Animator.AnimatorListener lis1 = new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animator) {}
                @Override public void onAnimationCancel(Animator animator) {}
                @Override public void onAnimationRepeat(Animator animator) {}
                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                    view.setTranslationX(0);
                    animatorSet2.start();
                }
            };
            Animator.AnimatorListener lis2 = new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animator) {}
                @Override public void onAnimationCancel(Animator animator) {}
                @Override public void onAnimationRepeat(Animator animator) {}
                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                    view.setTranslationX(0);
                    animatorSet.start();
                }
            };

            long fadeInDuration = 800;
            long scaleDownDuration = 800;
            long fadeOutDuration = 800;
            long delay = 1000;

            final ValueAnimator delayAnim = ObjectAnimator.ofFloat(view, "translationX", 0);
            delayAnim.setDuration(delay);
            final ValueAnimator fadeInAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeInAnim.setDuration(fadeInDuration);
            final ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f);
            scaleDownX.setDuration(scaleDownDuration);
            final ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f);
            scaleDownY.setDuration(scaleDownDuration);
            final ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 0.85f, 1f);
            scaleUpX.setDuration(scaleDownDuration);
            final ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 0.85f, 1f);
            scaleUpY.setDuration(scaleDownDuration);
            final ValueAnimator fadeOutAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            fadeOutAnim.setDuration(fadeOutDuration);

            final ValueAnimator delayAnim2 = ObjectAnimator.ofFloat(view, "translationX", 0);
            delayAnim2.setDuration(delay);
            final ValueAnimator fadeInAnim2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeInAnim2.setDuration(fadeInDuration);
            final ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f);
            scaleDownX2.setDuration(scaleDownDuration);
            final ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f);
            scaleDownY2.setDuration(scaleDownDuration);
            final ObjectAnimator scaleUpX2 = ObjectAnimator.ofFloat(view, "scaleX", 0.85f, 1f);
            scaleUpX2.setDuration(scaleDownDuration);
            final ObjectAnimator scaleUpY2 = ObjectAnimator.ofFloat(view, "scaleY", 0.85f, 1f);
            scaleUpY2.setDuration(scaleDownDuration);
            final ValueAnimator fadeOutAnim2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            fadeOutAnim2.setDuration(fadeOutDuration);
            view.setAlpha(0);
            animatorSet.setStartDelay(mToolTip.mEnterAnimation.getDuration());
            animatorSet.play(fadeInAnim);
            animatorSet.play(scaleDownX).with(scaleDownY).after(fadeInAnim);
            animatorSet.play(scaleUpX).with(scaleUpY).with(fadeOutAnim).after(scaleDownY);
            animatorSet.play(delayAnim).after(scaleUpY);

            animatorSet2.play(fadeInAnim2);
            animatorSet2.play(scaleDownX2).with(scaleDownY2).after(fadeInAnim2);
            animatorSet2.play(scaleUpX2).with(scaleUpY2).with(fadeOutAnim2).after(scaleDownY2);
            animatorSet2.play(delayAnim2).after(scaleUpY2);

            animatorSet.addListener(lis1);
            animatorSet2.addListener(lis2);
            animatorSet.start();
        }
    }
    private int getScreenWidth(){
        if (mActivity!=null) {
            Display display = mActivity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.x;
        } else {
            return 0;
        }
    }
}
