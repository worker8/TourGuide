package tourguide.tourguide

import android.graphics.Color
import android.view.View
import android.view.animation.Animation

/**
 * [Overlay] shows a tinted background to cover up the rest of the screen. A 'hole' will be made on this overlay to let users obtain focus on the targeted element.
 */
class Overlay @JvmOverloads constructor(var mDisableClick: Boolean = true, var backgroundColor: Int = Color.parseColor("#55000000"), var mStyle: Style = Style.CIRCLE) {
    var mDisableClickThroughHole: Boolean = false
    var mEnterAnimation: Animation? = null
    var mExitAnimation: Animation? = null
    var mHoleOffsetLeft = 0
    var mHoleOffsetTop = 0
    var mOnClickListener: View.OnClickListener? = null
    var mHoleRadius = NOT_SET
    var mPaddingDp = 10
    var mRoundedCornerRadiusDp = 0

    enum class Style {
        CIRCLE, RECTANGLE, ROUNDED_RECTANGLE, NO_HOLE
    }

    fun backgroundColor(block: () -> Int) {
        backgroundColor = block()
    }

    /**
     * Set to true if you want to block all user input to pass through this overlay, set to false if you want to allow user input under the overlay
     * @param yesNo
     * @return return [Overlay] instance for chaining purpose
     */
    fun disableClick(yesNo: Boolean): Overlay {
        mDisableClick = yesNo
        return this
    }

    fun disableClick(block: () -> Boolean) {
        mDisableClick = block()
    }

    /**
     * Set to true if you want to disallow the highlighted view to be clicked through the hole,
     * set to false if you want to allow the highlighted view to be clicked through the hole
     * @param yesNo
     * @return return Overlay instance for chaining purpose
     */
    fun disableClickThroughHole(yesNo: Boolean): Overlay {
        mDisableClickThroughHole = yesNo
        return this
    }

    fun disableClickThroughHole(block: () -> Boolean) {
        mDisableClickThroughHole = block()
    }

    fun setStyle(style: Style): Overlay {
        mStyle = style
        return this
    }

    fun style(block: () -> Style) {
        mStyle = block()
    }

    /**
     * Set enter animation
     * @param enterAnimation
     * @return return [Overlay] instance for chaining purpose
     */
    fun setEnterAnimation(enterAnimation: Animation): Overlay {
        mEnterAnimation = enterAnimation
        return this
    }

    /**
     * Set exit animation
     * @param exitAnimation
     * @return return [Overlay] instance for chaining purpose
     */
    fun setExitAnimation(exitAnimation: Animation): Overlay {
        mExitAnimation = exitAnimation
        return this
    }

    /**
     * Set [Overlay.mOnClickListener] for the [Overlay]
     * @param onClickListener
     * @return return [Overlay] instance for chaining purpose
     */
    fun setOnClickListener(onClickListener: View.OnClickListener): Overlay {
        mOnClickListener = onClickListener
        return this
    }

    fun onClickListener(block: () -> View.OnClickListener) {
        mOnClickListener = block()
    }

    /**
     * This method sets the hole's radius.
     * If this is not set, the size of view hole fill follow the max(view.width, view.height)
     * If this is set, it will take precedence
     * It only has effect when [Overlay.Style.CIRCLE] is chosen
     * @param holeRadius the radius of the view hole, setting 0 will make the hole disappear, in pixels
     * @return return [Overlay] instance for chaining purpose
     */
    fun setHoleRadius(holeRadius: Int): Overlay {
        mHoleRadius = holeRadius
        return this
    }


    /**
     * This method sets offsets to the hole's position relative the position of the targeted view.
     * @param offsetLeft left offset, in pixels
     * @param offsetTop top offset, in pixels
     * @return [Overlay] instance for chaining purpose
     */
    fun setHoleOffsets(offsetLeft: Int, offsetTop: Int): Overlay {
        mHoleOffsetLeft = offsetLeft
        mHoleOffsetTop = offsetTop
        return this
    }

    /**
     * This method sets the padding to be applied to the hole cutout from the overlay
     * @param paddingDp padding, in dp
     * @return [Overlay] intance for chaining purpose
     */
    fun setHolePadding(paddingDp: Int): Overlay {
        mPaddingDp = paddingDp
        return this
    }

    fun holePadding(block: () -> Int) {
        mPaddingDp = block()
    }

    /**
     * This method sets the radius for the rounded corner
     * It only has effect when [Overlay.Style.ROUNDED_RECTANGLE] is chosen
     * @param roundedCornerRadiusDp padding, in pixels
     * @return [Overlay] intance for chaining purpose
     */
    fun setRoundedCornerRadius(roundedCornerRadiusDp: Int): Overlay {
        mRoundedCornerRadiusDp = roundedCornerRadiusDp
        return this
    }

    fun roundedCornerRadius(block: () -> Int) {
        mRoundedCornerRadiusDp = block()
    }

    companion object {
        const val NOT_SET = -1
    }
}
