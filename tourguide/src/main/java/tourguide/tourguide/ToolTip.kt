package tourguide.tourguide

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.BounceInterpolator

class ToolTip() {
    var title: String
        private set
    var description: String
        private set
    var mBackgroundColor: Int = 0
    var mTextColor: Int = 0
    var mEnterAnimation: Animation
    var mExitAnimation: Animation? = null
    var mShadow: Boolean = false
    var mGravity: Int = 0
    var mOnClickListener: View.OnClickListener? = null
    var mCustomView: ViewGroup? = null
    var mWidth: Int = 0

    init {
        /* default values */
        title = ""
        description = ""
        mBackgroundColor = Color.parseColor("#3498db")
        mTextColor = Color.parseColor("#FFFFFF")

        mEnterAnimation = AlphaAnimation(0f, 1f)
        mEnterAnimation.duration = 1000
        mEnterAnimation.fillAfter = true
        mEnterAnimation.interpolator = BounceInterpolator()
        mShadow = true
        mWidth = -1

        // TODO: exit animation
        mGravity = Gravity.CENTER
    }

    constructor(init: ToolTip.() -> Unit) : this() {
        init()
    }

    fun title(block: () -> String) {
        title = block()
    }

    fun description(block: () -> String) {
        description = block()
    }

    fun backgroundColor(block: () -> Int) {
        mBackgroundColor = block()
    }

    fun textColor(block: () -> Int) {
        mTextColor = block()
    }

    fun gravity(block: () -> Int) {
        mGravity = block()
    }

    fun shadow(block: () -> Boolean) {
        mShadow = block()
    }

    fun enterAnimation(block: () -> Animation) {
        mEnterAnimation = block()
    }

    fun exitAnimation(block: () -> Animation) {
        mExitAnimation = block()
    }

    /**
     * Set title text
     * @param title
     * @return return ToolTip instance for chaining purpose
     */
    fun setTitle(title: String): ToolTip {
        this.title = title
        return this
    }

    /**
     * Set description text
     * @param description
     * @return return ToolTip instance for chaining purpose
     */
    fun setDescription(description: String): ToolTip {
        this.description = description
        return this
    }

    /**
     * Set background color
     * @param backgroundColor
     * @return return ToolTip instance for chaining purpose
     */
    fun setBackgroundColor(backgroundColor: Int): ToolTip {
        mBackgroundColor = backgroundColor
        return this
    }

    /**
     * Set text color
     * @param textColor
     * @return return ToolTip instance for chaining purpose
     */
    fun setTextColor(textColor: Int): ToolTip {
        mTextColor = textColor
        return this
    }

    /**
     * Set enter animation
     * @param enterAnimation
     * @return return ToolTip instance for chaining purpose
     */
    fun setEnterAnimation(enterAnimation: Animation): ToolTip {
        mEnterAnimation = enterAnimation
        return this
    }

    /**
     * Set the gravity, the setGravity is centered relative to the targeted button
     * @param gravity Gravity.CENTER, Gravity.TOP, Gravity.BOTTOM, etc
     * @return return ToolTip instance for chaining purpose
     */
    fun setGravity(gravity: Int): ToolTip {
        mGravity = gravity
        return this
    }

    /**
     * Set if you want to have setShadow
     * @param shadow
     * @return return ToolTip instance for chaining purpose
     */
    fun setShadow(shadow: Boolean): ToolTip {
        mShadow = shadow
        return this
    }

    /**
     * Method to set the width of the ToolTip
     * @param px desired width of ToolTip in pixels
     * @return ToolTip instance for chaining purposes
     */
    fun setWidth(px: Int): ToolTip {
        if (px >= 0) mWidth = px
        return this
    }

    fun setOnClickListener(onClickListener: View.OnClickListener): ToolTip {
        mOnClickListener = onClickListener
        return this
    }

    fun getCustomView(): ViewGroup? {
        return mCustomView
    }

    fun setCustomView(view: ViewGroup): ToolTip {
        mCustomView = view
        return this
    }
}