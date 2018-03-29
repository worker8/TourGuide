package tourguide.tourguide

import android.graphics.Color
import android.view.Gravity

class Pointer @JvmOverloads constructor(gravity: Int = Gravity.CENTER, color: Int = Color.parseColor("#FFFFFF")) {
    var mGravity = Gravity.CENTER
    var mColor = Color.WHITE

    /**
     * Set color
     * @param color
     * @return return Pointer instance for chaining purpose
     */
    fun setColor(color: Int): Pointer {
        mColor = color
        return this
    }

    /**
     * Set gravity
     * @param gravity
     * @return return Pointer instance for chaining purpose
     */
    fun setGravity(gravity: Int): Pointer {
        mGravity = gravity
        return this
    }
}
