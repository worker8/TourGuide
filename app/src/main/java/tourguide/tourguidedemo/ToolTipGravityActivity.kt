package tourguide.tourguidedemo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_tooltip_gravity_i.*
import tourguide.tourguide.TourGuide


class ToolTipGravityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        /* Get parameters from main activity */
        val tooltipNum = intent.getIntExtra(TOOLTIP_NUM, 1)

        super.onCreate(savedInstanceState)

        val gravity = when (tooltipNum) {
            1 -> {
                setContentView(R.layout.activity_tooltip_gravity_i)
                Gravity.RIGHT or Gravity.BOTTOM
            }
            2 -> {
                setContentView(R.layout.activity_tooltip_gravity_ii)
                Gravity.LEFT or Gravity.BOTTOM
            }
            3 -> {
                setContentView(R.layout.activity_tooltip_gravity_iii)
                Gravity.LEFT or Gravity.TOP
            }
            else -> {
                setContentView(R.layout.activity_tooltip_gravity_iv)
                Gravity.RIGHT or Gravity.TOP
            }
        }

        val tourGuide =
                TourGuide.create(this) {
                    toolTip {
                        title { "Welcome!" }
                        description { "Click on Get Started to begin..." }
                        backgroundColor { Color.parseColor("#2980b9") }
                        textColor { Color.parseColor("#FFFFFF") }
                        gravity { gravity }
                        shadow { true }
                    }
                    pointer {}
                    overlay {}
                }.playOn(button)

        button.setOnClickListener { tourGuide.cleanUp() }

    }

    companion object {
        val TOOLTIP_NUM = "tooltip_num"
    }

}
