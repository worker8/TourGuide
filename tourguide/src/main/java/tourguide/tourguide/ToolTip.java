package tourguide.tourguide;

import android.graphics.Color;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;

/**
 * Created by tanjunrong on 6/17/15.
 */
public class ToolTip {
    public String mTitle, mDescription;
    public int mBackgroundColor, mTextColor;
    public Animation mEnterAnimation, mExitAnimation;
    public boolean mShadow;
    public int mGravity;

    public ToolTip(){
        /* default values */
        mTitle = "";
        mDescription = "";
        mBackgroundColor = Color.parseColor("#3498db");
        mTextColor = Color.parseColor("#FFFFFF");

        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(1000);
        mEnterAnimation.setFillAfter(true);
        mEnterAnimation.setInterpolator(new BounceInterpolator());
        mShadow = true;

        // TODO: exit animation
        mGravity = Gravity.CENTER;
    }
    /**
     * Set title text
     * @param title
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip title(String title){
        mTitle = title;
        return this;
    }

    /**
     * Set description text
     * @param description
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip description(String description){
        mDescription = description;
        return this;
    }

    /**
     * Set background color
     * @param backgroundColor
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip backgroundColor(int backgroundColor){
        mBackgroundColor = backgroundColor;
        return this;
    }

    /**
     * Set text color
     * @param textColor
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip textColor(int textColor){
        mTextColor = textColor;
        return this;
    }

    /**
     * Set enter animation
     * @param enterAnimation
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip enterAnimation(Animation enterAnimation){
        mEnterAnimation = enterAnimation;
        return this;
    }
    /**
     * Set exit animation
     * @param exitAnimation
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip exitAnimation(Animation exitAnimation){
        mExitAnimation = exitAnimation;
        return this;
    }
    /**
     * Set the gravity, the gravity is centered relative to the targeted button
     * @param gravity Gravity.CENTER, Gravity.TOP, Gravity.BOTTOM, etc
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip gravity(int gravity){
        mGravity = gravity;
        return this;
    }
    /**
     * Set if you want to have shadow
     * @param shadow
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip shadow(boolean shadow){
        mShadow = shadow;
        return this;
    }
}
