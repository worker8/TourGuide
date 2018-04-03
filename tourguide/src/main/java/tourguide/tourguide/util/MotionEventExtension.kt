package tourguide.tourguide.util

import android.view.MotionEvent

/**
 * Show an event in the LogCat view, for debugging
 */
fun MotionEvent.dumpEvent() {
    val event = this
    val names = arrayOf("DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?")
    val sb = StringBuilder()
    val action = event.action
    val actionCode = action and MotionEvent.ACTION_MASK
    sb.append("event ACTION_").append(names[actionCode])
    if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
        sb.append("(pid ").append(
                action shr MotionEvent.ACTION_POINTER_ID_SHIFT)
        sb.append(")")
    }
    sb.append("[")
    for (i in 0 until event.pointerCount) {
        sb.append("#").append(i)
        sb.append("(pid ").append(event.getPointerId(i))
        sb.append(")=").append(event.getX(i).toInt())
        sb.append(",").append(event.getY(i).toInt())
        if (i + 1 < event.pointerCount)
            sb.append(";")
    }
    sb.append("]")
}