package tourguide.tourguide;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

/**
 * Created by tanjunrong on 6/17/15.
 */
public class ToolTip {
    public String mTitle, mDescription;
    public int mBackgroundColor, mTextColor;
    public Animation mEnterAnimation, mExitAnimation;
    public boolean mShadow;
    public int mGravity;
    public View.OnClickListener mOnClickListener;
    public int mLayoutResource;

    public ToolTip(){
        /* default values */
        mLayoutResource = R.layout.tooltip;

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
    public ToolTip setTitle(String title){
        mTitle = title;
        return this;
    }

    /**
     * Set description text
     * @param description
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setDescription(String description){
        mDescription = description;
        return this;
    }

    /**
     * Set background color
     * @param backgroundColor
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setBackgroundColor(int backgroundColor){
        mBackgroundColor = backgroundColor;
        return this;
    }

    /**
     * Set text color
     * @param textColor
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setTextColor(int textColor){
        mTextColor = textColor;
        return this;
    }

    /**
     * Set enter animation
     * @param enterAnimation
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setEnterAnimation(Animation enterAnimation){
        mEnterAnimation = enterAnimation;
        return this;
    }
    /**
     * Set exit animation
     * @param exitAnimation
     * @return return ToolTip instance for chaining purpose
     */
//    TODO:
//    public ToolTip setExitAnimation(Animation exitAnimation){
//        mExitAnimation = exitAnimation;
//        return this;
//    }
    /**
     * Set the gravity, the setGravity is centered relative to the targeted button
     * @param gravity Gravity.CENTER, Gravity.TOP, Gravity.BOTTOM, etc
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setGravity(int gravity){
        mGravity = gravity;
        return this;
    }
    /**
     * Set if you want to have setShadow
     * @param shadow
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setShadow(boolean shadow){
        mShadow = shadow;
        return this;
    }
    /**
     * Set if you want to set a custom layout.
     * @param resource
     * @return return ToolTip instance for chaining purpose
     */
    public ToolTip setLayout(int resource, Context context){
        validateLayoutResource(resource, context);
        mLayoutResource = resource;
        return this;
    }

    public ToolTip setOnClickListener(View.OnClickListener onClickListener){
        mOnClickListener = onClickListener;
        return this;
    }

    private void validateLayoutResource(int resource, Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View layout = layoutInflater.inflate(resource, null);

        // Make sure the layout contains all necessary components
        View toolTipContainer = layout.findViewById(R.id.toolTip_container);
        TextView toolTipTitle = (TextView) layout.findViewById(R.id.title);
        TextView toolTipDescription = (TextView) layout.findViewById(R.id.description);

        if (toolTipContainer == null) {
            throw new IllegalArgumentException("The supplied layout does not contain a View component with id 'toolTip_container'");
        }
        if (toolTipTitle == null) {
            throw new IllegalArgumentException("The supplied layout does not contain a TextView component with id 'title'");
        }
        if (toolTipDescription == null) {
            throw new IllegalArgumentException("The supplied layout does not contain a TextView component with id 'description'");
        }
    }
}
