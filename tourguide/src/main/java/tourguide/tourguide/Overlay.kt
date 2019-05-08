package tourguide.tourguide

import android.graphics.Color
import android.view.View
import android.view.animation.Animation

/**
 * [Overlay] shows a tinted background to cover up the rest of the screen. A 'hole' will be made on this overlay to let users obtain focus on the targeted element.
 */
class Overlay @JvmOverloads constructor(var mDisableClick: Boolean = true, var backgroundColor: Int = Color.parseColor(DEFAULT_BG_COLOR)) {
    var mEnterAnimation: Animation? = null
    var mExitAnimation: Animation? = null
    var mOnClickListener: View.OnClickListener? = null

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

    companion object {
        const val DEFAULT_BG_COLOR = "#55000000"
    }
}
