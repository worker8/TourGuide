package tourguide.tourguidedemo

import android.app.Activity
import android.graphics.Point
import android.test.ActivityInstrumentationTestCase2
import android.view.View
import org.junit.Assert
import tourguide.instrumentation.test.ToolTipMeasureTestActivity

class ToolTilMeasureTest : ActivityInstrumentationTestCase2<ToolTipMeasureTestActivity>(ToolTipMeasureTestActivity::class.java) {
    private var mActivity: ToolTipMeasureTestActivity? = null
    private var mToolTip: View? = null

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()

        // Starts the activity under test using the default Intent with:
        // action = {@link Intent#ACTION_MAIN}
        // flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
        // All other fields are null or empty.
        mActivity = activity
        mToolTip = mActivity!!.tourGuide.toolTipView
    }

    fun testNoCrash() {
        val isOutOfLeftBound = mToolTip!!.x < 0
        val isOutOfRightBound = mToolTip!!.x + mToolTip!!.width > getScreenWidth(mActivity!!)
        Assert.assertFalse("x of ToolTip is out of screen boundary", isOutOfLeftBound || isOutOfRightBound)

        val isOutOfTopBound = mToolTip!!.y < 0
        val isOutOfBottomBound = mToolTip!!.y + mToolTip!!.height > getScreenHeight(mActivity!!)
        Assert.assertFalse("y of ToolTip is out of screen boundary", isOutOfTopBound || isOutOfBottomBound)
    }

    private fun getScreenWidth(activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    private fun getScreenHeight(activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
    }

}
