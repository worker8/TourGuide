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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


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
        playAnimation();
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
    private int getXBasedOnGravity(int width){
        if((mGravity & Gravity.RIGHT) == Gravity.RIGHT){
            return (int)mHighlightedView.getX()+mHighlightedView.getWidth()-width;
        } else if ((mGravity & Gravity.LEFT) == Gravity.LEFT) {
            return (int)mHighlightedView.getX();
         }else { // this is center
            return (int)mHighlightedView.getX()+mHighlightedView.getWidth()/2-width/2;
        }
    }
    private int getYBasedOnGravity(int height){
        if((mGravity & Gravity.BOTTOM) == Gravity.BOTTOM){
            return (int)mHighlightedView.getY()+mHighlightedView.getHeight()-height;
        } else if ((mGravity & Gravity.TOP) == Gravity.TOP) {
            return (int)mHighlightedView.getY();
        }else { // this is center
            return (int)mHighlightedView.getY()+mHighlightedView.getHeight()/2-height/2;
        }
    }
    private void playAnimation(){
//        TODO: throw exception if either mActivity, mDuration, mHighlightedView is null
        final ViewTreeObserver viewTreeObserver = mHighlightedView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // make sure this only run once
                mHighlightedView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.d("ddw", "HighlightedView.getHeight(): " + mHighlightedView.getHeight());
                Log.d("ddw", "HighlightedView.getWidth(): " + mHighlightedView.getWidth());

                // Initialize
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                layoutParams.gravity = Gravity.BOTTOM;

                mFrameLayout = new FrameLayoutWithHole(mActivity, mMotionType);
                ImageView imageView = new ImageView(mActivity);
                imageView.setImageResource(R.drawable.ic_launcher1);

                // measure size of image to be placed
                imageView.measure(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                int width = imageView.getMeasuredWidth();
                int height = imageView.getMeasuredHeight();
                Log.d("ddw","[tut] width: "+width);
                Log.d("ddw", "[tut] height: " + height);

                // calculate and set location
//                params.gravity = Gravity.RIGHT|Gravity.TOP;
                // This is Gravity.RIGHT:
                Log.d("ddw","mHighlightedView.getX(): "+mHighlightedView.getX());
                Log.d("ddw","mHighlightedView.getWidth(): "+mHighlightedView.getWidth());
                Log.d("ddw", "width: " + width);
                params.setMargins(getXBasedOnGravity(width),getYBasedOnGravity(height),0,0);
//                params.setMargins((int)mHighlightedView.getX()+mHighlightedView.getWidth()/2-width/2,(int)mHighlightedView.getY()+mHighlightedView.getHeight()/2-height/2,0,0);
                mFrameLayout.addView(imageView, params);
                mFrameLayout.setBackgroundColor(mOverlayBackgroundColor);

                if (mDisableClick) {
                    mFrameLayout.setViewHole(mHighlightedView);
                    mFrameLayout.setSoundEffectsEnabled(false);
                    mFrameLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("ddw", "disable, do nothing");
                        }
                    });
                }
                /* setup instruction view */
                if (mTitle!=null || mDescription!=null) {
                    LinearLayout.LayoutParams layoutParamsInstruction = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    LinearLayout.LayoutParams layoutParamsInstruction1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    FrameLayout.LayoutParams layoutParamsInstruction2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    layoutParamsInstruction2.gravity = Gravity.BOTTOM;
                    float density = mActivity.getResources().getDisplayMetrics().density;
                    int sideMargin = (int)(20 * density); // margin in pixels
                    int topAndDownMargin = (int)(30 * density); // margin in pixels

                    layoutParamsInstruction.setMargins(sideMargin, topAndDownMargin, sideMargin, 0);
                    layoutParamsInstruction1.setMargins(sideMargin, 0, sideMargin, topAndDownMargin);
                    LinearLayout instructionLinearLayout = new LinearLayout(mActivity);

                    TextView titleTV = new TextView(mActivity);
                    TextView descriptionTV = new TextView(mActivity);
                    if (mTitle!=null) {
                        titleTV.setText(mTitle);
                        titleTV.setTextSize(20);
                        titleTV.setTextColor(Color.parseColor("#3399FF"));
                    }
                    if (mDescription!=null) {
                        descriptionTV.setText(mDescription);
                        descriptionTV.setTextSize(16);
                        descriptionTV.setTextColor(Color.parseColor("#333333"));
                    }
                    instructionLinearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    instructionLinearLayout.setOrientation(LinearLayout.VERTICAL);
                    View blueLine = new View(mActivity);
                    LinearLayout.LayoutParams blueLineLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(2*density));
                    blueLine.setBackgroundColor(Color.parseColor("#3399FF"));
                    instructionLinearLayout.addView(blueLine, blueLineLP);
                    instructionLinearLayout.addView(titleTV, layoutParamsInstruction);
                    instructionLinearLayout.addView(descriptionTV, layoutParamsInstruction1);
                    mFrameLayout.addView(instructionLinearLayout, layoutParamsInstruction2);
                }
                ((ViewGroup) mActivity.getWindow().getDecorView()).addView(mFrameLayout, layoutParams);
                performAnimationOn(imageView);
            }
        });

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
    /**
     * Clean up the tutorial that is added to the activity
     */
    public void cleanUp(){
        if (mFrameLayout.getParent()!=null){
            ((ViewGroup)mFrameLayout.getParent()).removeView(mFrameLayout);
        }
    }
}
