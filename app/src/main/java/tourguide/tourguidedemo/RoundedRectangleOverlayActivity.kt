package tourguide.tourguidedemo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_overlay_customization.*
import tourguide.tourguide.Config
import tourguide.tourguide.TourGuide


class RoundedRectangleOverlayActivity : AppCompatActivity() {
    lateinit var tourGuide: TourGuide
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overlay_customization)
        tourGuide = TourGuide.create(this) {
            overlay {
                backgroundColor { Color.parseColor("#AAFF0000") }
            }
        }.playOn(button) {
            pointer { }
            toolTip {
                title { "Hello!" }
                description { "Click to view tutorial. Next button is disabled until tutorial is viewed" }
            }
            canClickThroughHole { true }
            shape { Config.Shape.RoundedRectangle(8, 10) }
        }.show()
        nextButton.setOnClickListener { Toast.makeText(this, "BOOM!", Toast.LENGTH_SHORT).show() }

        button.setOnClickListener { tourGuide.cleanUp() }
    }
}
