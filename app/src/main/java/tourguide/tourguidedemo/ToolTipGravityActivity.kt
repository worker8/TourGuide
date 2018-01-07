package tourguide.tourguidedemo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.ActionBarActivity
import android.view.Gravity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_tooltip_gravity_i.*

import tourguide.tourguide.Overlay
import tourguide.tourguide.Pointer
import tourguide.tourguide.ToolTip
import tourguide.tourguide.TourGuide


class ToolTipGravityActivity : ActionBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        /* Get parameters from main activity */
        val tooltipNum = intent.getIntExtra(TOOLTIP_NUM, 1)

        super.onCreate(savedInstanceState)
        val gravity: Int

        when (tooltipNum) {
            1 -> {
                setContentView(R.layout.activity_tooltip_gravity_i)
                gravity = Gravity.RIGHT or Gravity.BOTTOM
            }
            2 -> {
                setContentView(R.layout.activity_tooltip_gravity_ii)
                gravity = Gravity.LEFT or Gravity.BOTTOM
            }
            3 -> {
                setContentView(R.layout.activity_tooltip_gravity_iii)
                gravity = Gravity.LEFT or Gravity.TOP
            }
            else -> {
                setContentView(R.layout.activity_tooltip_gravity_iv)
                gravity = Gravity.RIGHT or Gravity.TOP
            }
        }

        val toolTip = ToolTip().setTitle("Welcome!")
                .setDescription("Click on Get Started to begin...")
                .setBackgroundColor(Color.parseColor("#2980b9"))
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setGravity(gravity).setShadow(true)

        val mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.CLICK)
                .setPointer(Pointer())
                .setToolTip(toolTip)
                .setOverlay(Overlay())
                .playOn(button)

        button.setOnClickListener { mTutorialHandler.cleanUp() }
    }

    companion object {
        val TOOLTIP_NUM = "tooltip_num"
    }
}
