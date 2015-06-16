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
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;


/**
 * Created by tanjunrong on 2/10/15.
 */
public class AnimateTutorial {
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
    private String mTitle, mDescription;
    /* Static builder */
    public static AnimateTutorial init(Activity activity){
        return new AnimateTutorial(activity);
    }

    /* Constructor */
    public AnimateTutorial(Activity activity){
        mActivity = activity;
    }

    /**
     * Setter for the animation to be used
     * @param technique Animation to be used
     * @return return AnimateTutorial instance for chaining purpose
     */
    public AnimateTutorial with(Technique technique){
        mTechnique = technique;
        return this;
    }
    /**
     * Set title text
     * @param title
     * @return return AnimateTutorial instance for chaining purpose
     */
    public AnimateTutorial title(String title){
        mTitle = title;
        return this;
    }

    /**
     * Set description text
     * @param description
     * @return return AnimateTutorial instance for chaining purpose
     */
    public AnimateTutorial description(String description){
        mDescription = description;
        return this;
    }
    /**
     * Sets which motion type is motionType
     * @param motionType
     * @return return AnimateTutorial instance for chaining purpose
     */
    public AnimateTutorial motionType(MotionType motionType){
        mMotionType = motionType;
        return this;
    }

    /**
     * Sets the duration
     * @param duration duration for the animation to happen, in miliseconds
     * @return return AnimateTutorial instance for chaining purpose
     */
    public AnimateTutorial duration(int duration){
        mDuration = duration;
        return this;
    }

    /**
     * Sets the duration
     * @param view the view in which the tutorial button will be placed on top of
     * @return return AnimateTutorial instance for chaining purpose
     */
    public AnimateTutorial playOn(View view){
        mHighlightedView = view;
        setupView();
        return this;
    }
    /**
     * Sets the gravity
     * @param gravity Gravity.CENTER for example, the
     * @return return AnimateTutorial instance for chaining purpose
     */
    public AnimateTutorial gravity(int gravity){
        mGravity = gravity;
        return this;
    }
    /**
     * Sets the background color for the overlay
     * @param color the color of the background, default to transparent
     * @return return AnimateTutorial instance for chaining purpose
     */
    public AnimateTutorial overlayColor(int color){
        mOverlayBackgroundColor = color;
        return this;
    }

    /**
     * Setting true will disable the clicking on any view behind the target view in tutorial
     * @param disable true to disable clicking, false to enable clicking, default to false
     * @return return AnimateTutorial instance for chaining purpose
     */
    public AnimateTutorial disableClick(boolean disable){
        mDisableClick = disable;
        return this;
    }

    /**
     * Clean up the tutorial that is added to the activity
     */
    public void cleanUp(){
        if (mFrameLayout.getParent()!=null){
            ((ViewGroup)mFrameLayout.getParent()).removeView(mFrameLayout);
            ((ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content)).removeView(mDescriptionViewGroup);
        }
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
    final int description_enter_animation_duration = 700;
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

                /* setup instruction view */
                setupInstructionViews(mFrameLayout);

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
    private void setupInstructionViews(FrameLayoutWithHole frameLayoutWithHole){
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.BOTTOM;
        if (mTitle!=null || mDescription!=null) {
            LayoutInflater layoutInflater = mActivity.getLayoutInflater();
            mDescriptionViewGroup = layoutInflater.inflate(R.layout.description, null);
            TextView titleTV = (TextView)mDescriptionViewGroup.findViewById(R.id.title);
            TextView mDescriptionTV = (TextView)mDescriptionViewGroup.findViewById(R.id.description);
            titleTV.setText(mTitle);

            TranslateAnimation translation;
            translation = new TranslateAnimation(0f, 0F, 200f, 0f);
            translation.setDuration(description_enter_animation_duration);
            translation.setFillAfter(true);
            translation.setInterpolator(new BounceInterpolator());
            mDescriptionViewGroup.startAnimation(translation);

            mDescriptionTV.setText(mDescription);
            ((ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content)).addView(mDescriptionViewGroup, layoutParams);
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
            animatorSet.setStartDelay(description_enter_animation_duration);
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
