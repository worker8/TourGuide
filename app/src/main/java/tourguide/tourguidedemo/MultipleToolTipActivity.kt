package tourguide.tourguidedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_multiple_tooltip.*
import tourguide.tourguide.Pointer
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
            pointer { Pointer() }
            toolTip {
                title { "Hey!" }
                description { "I'm the top guy" }
                gravity { Gravity.RIGHT }
            }
        }.playOn(button)

        val tourGuide2 = TourGuide.create(this) {
            pointer { Pointer() }
            toolTip {
                title { "Hey!" }
                description { "I'm the bottom guy" }
                gravity { Gravity.TOP or Gravity.LEFT }
            }
        }.playOn(button2)

        button.setOnClickListener { tourGuide.cleanUp() }
        button2.setOnClickListener { tourGuide2.cleanUp() }
    }
}
