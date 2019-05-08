package tourguide.tourguide

import android.graphics.Path
import android.view.View

private typealias OnClickListener = (View) -> Unit

class Config {

    var canClickThroughHole: Boolean = true
        private set
    var onHoleClickListener: OnClickListener? = null
        private set
    var shape: Shape = Shape.Circle()
        private set
    var offsetLeft: Int = 0
        private set
    var offsetTop: Int = 0
        private set
    var pointer: Pointer? = null
        private set
    var motionType: MotionType = MotionType.CLICK_ONLY
        private set
    var toolTip: ToolTip? = null
        private set

    fun setCanClickThroughHole(yesNo: Boolean) {
        canClickThroughHole = yesNo
    }

    fun canClickThroughHole(block: () -> Boolean) {
        canClickThroughHole = block()
    }

    fun setOnHoleClickListener(listener: OnClickListener?) {
        onHoleClickListener = listener
    }

    fun setShape(shape: Shape) {
        this.shape = shape
    }

    fun shape(block: () -> Shape) {
        shape = block()
    }

    /**
     * This method sets offsets to the hole's position relative the position of the targeted view.
     * @param offsetLeft left offset, in pixels
     * @param offsetTop top offset, in pixels
     */
    fun setHoleOffsets(offsetLeft: Int, offsetTop: Int) {
        this.offsetLeft = offsetLeft
        this.offsetTop = offsetTop
    }

    fun setPointer(pointer: Pointer) {
        this.pointer = pointer
    }

    fun pointer(block: Pointer.() -> Unit) {
        this.pointer = Pointer().apply(block)
    }

    /**
     * Sets which motion type is motionType
     * @param motionType
     * @return return TourGuide instance for chaining purpose
     */
    fun setMotionType(motionType: MotionType) {
        this.motionType = motionType
    }

    fun motionType(block: () -> MotionType) {
        this.motionType = block()
    }

    /**
     * Set the toolTipView
     * @param toolTip this toolTipView object should contain the attributes of the ToolTip, such as, the title text, and the description text, background color, etc
     */
    fun setToolTip(toolTip: ToolTip) {
        this.toolTip = toolTip
    }

    fun toolTip(block: ToolTip.() -> Unit) {
        toolTip = ToolTip().apply(block)
    }

    sealed class Shape {
        class Circle(val holeRadius: Int = NOT_SET) : Shape() {

            companion object {
                const val NOT_SET = -1
            }
        }

        class RoundedRectangle(val cornerRadius: Int = NOT_SET, padding: Int = 0) : Rectangle(padding) {

            companion object {
                const val NOT_SET = -1
            }
        }

        class DrawPath(val path: Path) : Shape()
        open class Rectangle(val padding: Int = 0) : Shape()
        object NoHole : Shape()
    }

    /**
     * This describes the allowable motion, for example if you want the users to learn about clicking, but want to stop them from swiping, then use CLICK_ONLY
     */
    enum class MotionType {
        ALLOW_ALL, CLICK_ONLY, SWIPE_ONLY
    }
}