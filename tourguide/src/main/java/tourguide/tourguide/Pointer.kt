package tourguide.tourguide

import android.graphics.Color
import android.view.Gravity

class Pointer @JvmOverloads constructor(
        var gravity: Int = Gravity.CENTER,
        var color: Int = Color.parseColor("#FFFFFF")
) {

    var technique: Technique = Technique.CLICK

    fun gravity(block: () -> Int) {
        gravity = block()
    }

    fun color(block: () -> Int) {
        color = block()
    }

    fun technique(block: () -> Technique) {
        technique = block()
    }

    /**
     * This describes the animation techniques
     */
    enum class Technique {
        CLICK, HORIZONTAL_LEFT, HORIZONTAL_RIGHT, VERTICAL_UPWARD, VERTICAL_DOWNWARD
    }
}