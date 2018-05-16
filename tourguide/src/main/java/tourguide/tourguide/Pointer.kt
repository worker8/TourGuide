package tourguide.tourguide

import android.graphics.Color
import android.view.Gravity

class Pointer @JvmOverloads constructor(var gravity: Int = Gravity.CENTER, var color: Int = Color.parseColor("#FFFFFF")) {

    fun gravity(block: () -> Int) {
        gravity = block()
    }

    fun color(block: () -> Int) {
        color = block()
    }
}