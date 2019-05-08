package tourguide.instrumentation.test

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_tooltip_gravity_i.*
import tourguide.tourguide.Overlay
import tourguide.tourguide.Pointer
import tourguide.tourguide.TourGuide
import tourguide.tourguidedemo.R


class ToolTipMeasureTestActivity : AppCompatActivity() {
    lateinit var tourGuide: TourGuide

    override fun onCreate(savedInstanceState: Bundle?) {
        /* Get parameters from main activity */
        val intent = intent
        val tooltipNumber = intent.getIntExtra(TOOLTIP_NUM, 1)

        super.onCreate(savedInstanceState)
        val gravity = when (tooltipNumber) {
            1 -> {
                setContentView(R.layout.activity_tooltip_gravity_i)
                Gravity.END or Gravity.BOTTOM
            }
            2 -> {
                setContentView(R.layout.activity_tooltip_gravity_ii)
                Gravity.START or Gravity.BOTTOM
            }
            3 -> {
                setContentView(R.layout.activity_tooltip_gravity_iii)
                Gravity.START or Gravity.TOP
            }
            else -> {
                setContentView(R.layout.activity_tooltip_gravity_iv)
                Gravity.END or Gravity.TOP
            }
        }

        tourGuide = TourGuide.create(this) {
            overlay { Overlay() }
        }.playOn(button) {
            pointer { }
            toolTip {
                title { "Welcome!" }
                description { "This is a really really long title....This is a really really long title....This is a really really long title....This is a really really long title....This is a really really long title....This is a really really long title....This is a really really long title...." }
                backgroundColor { Color.parseColor("#2980b9") }
                textColor { Color.parseColor("#FFFFFF") }
                gravity { gravity }
                shadow { true }
            }
        }.show()

        button.setOnClickListener { tourGuide.cleanUp() }
    }

    companion object {
        const val TOOLTIP_NUM = "tooltip_num"
    }
}
