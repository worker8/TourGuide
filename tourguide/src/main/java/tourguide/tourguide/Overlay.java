package tourguide.tourguide;

import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by tanjunrong on 6/20/15.
 */
public class Overlay {
    public int mBackgroundColor;
    public boolean mDisableClick;
    public Style mStyle;
    public Animation mEnterAnimation, mExitAnimation;
    public View.OnClickListener mOnClickListener;

    public enum Style {
        Circle, Rectangle
    }
    public Overlay() {
        this(true, Color.parseColor("#55000000"), Style.Circle);
    }

    public Overlay(boolean disableClick, int backgroundColor, Style style) {
        mDisableClick = disableClick;
        mBackgroundColor = backgroundColor;
        mStyle = style;
    }

    /**
     * Set background color
     * @param backgroundColor
     * @return return ToolTip instance for chaining purpose
     */
    public Overlay setBackgroundColor(int backgroundColor){
        mBackgroundColor = backgroundColor;
        return this;
    }

    /**
     * Set to true if you want to block all user input to pass through this overlay, set to false if you want to allow user input under the overlay
     * @param yes_no
     * @return return Overlay instance for chaining purpose
     */
    public Overlay disableClick(boolean yes_no){
        mDisableClick = yes_no;
        return this;
    }

    public Overlay setStyle(Style style){
        mStyle = style;
        return this;
    }

    /**
     * Set enter animation
     * @param enterAnimation
     * @return return Overlay instance for chaining purpose
     */
    public Overlay setEnterAnimation(Animation enterAnimation){
        mEnterAnimation = enterAnimation;
        return this;
    }
    /**
     * Set exit animation
     * @param exitAnimation
     * @return return Overlay instance for chaining purpose
     */
    public Overlay setExitAnimation(Animation exitAnimation){
        mExitAnimation = exitAnimation;
        return this;
    }

    /**
     * Set onClickListener for the Overlay
     * @param onClickListener
     * @return return Overlay instance for chaining purpose
     */
    public Overlay setOnClickListener(View.OnClickListener onClickListener){
        mOnClickListener=onClickListener;
        return this;
    }
}
