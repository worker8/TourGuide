package tourguide.tourguidedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_multiple_tooltip.*
import tourguide.tourguide.Config
import tourguide.tourguide.TourGuide

/**
 * Note that currently multiple Overlay doesn't work well, but multiple ToolTip is working fine
 * Therefore, if you want to use multiple ToolTip, please switch off the Overlay by .setOverlay(null)
 */
class MultipleToolTipActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_multiple_tooltip)

        // the return handler is used to manipulate the cleanup of all the tutorial elements
        val tourGuide = TourGuide.create(this) {
            overlay { }
        }.playOn(button) {
            pointer { }
            shape { Config.Shape.Rectangle(10) }
            toolTip {
                title { "Hey!" }
                description { "I'm the top guy" }
                gravity { Gravity.END }
            }
        }.playOn(button2) {
            pointer { }
            toolTip {
                title { "Hey!" }
                description { "I'm the bottom guy" }
                gravity { Gravity.TOP or Gravity.START }
            }
        }.show()

        button.setOnClickListener { tourGuide.clean(button) }
        button2.setOnClickListener { tourGuide.clean(button2) }
    }
}
