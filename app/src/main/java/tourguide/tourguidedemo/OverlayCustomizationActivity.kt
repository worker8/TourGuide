package tourguide.tourguidedemo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_overlay_customization.*
import tourguide.tourguide.Overlay
import tourguide.tourguide.TourGuide


class OverlayCustomizationActivity : AppCompatActivity() {
    lateinit var tourGuide: TourGuide
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overlay_customization)

        // the return handler is used to manipulate the cleanup of all the tutorial elements
        tourGuide = TourGuide.create(this) {
            pointer {}
            toolTip {
                title { "Hello!" }
                description { "Click to view tutorial. Next button is disabled until tutorial is viewed" }
            }
            overlay {
                disableClick { false }
                disableClickThroughHole { false }
                style { Overlay.Style.RECTANGLE }
                backgroundColor { Color.parseColor("#AAFF0000") }
                onClickListener { View.OnClickListener { tourGuide.cleanUp() } }
            }
        }.playOn(button)

        nextButton.setOnClickListener { Toast.makeText(this, "BOOM!", Toast.LENGTH_LONG).show() }
        button.setOnClickListener { tourGuide.cleanUp() }
    }
}
