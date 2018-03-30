package tourguide.tourguidedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_basic.*
import tourguide.tourguide.TourGuide


class NoPointerActivity : AppCompatActivity() {
    lateinit var tourGuide: TourGuide
    override fun onCreate(savedInstanceState: Bundle?) {
        /* Get parameters from main activity */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)

        button1.text = "Purchase"

//        val toolTip = ToolTip().setTitle("Expensive Item").setDescription("Click 'purchase' only when you are ready\nClick on the OVERLAY to dismiss")
//        val overlay = Overlay().disableClickThroughHole(true).setOnClickListener(View.OnClickListener { tourguide.cleanUp() })
//        // the return handler is used to manipulate the cleanup of all the tutorial elements
//        val tourGuide = TourGuide.init(this).with(TourGuide.Technique.CLICK)
//                .setToolTip(toolTip)

//        tourGuide.overlay = overlay
//        tourGuide.playOn(button1)

//        button2.setOnClickListener { tourGuide.playOn(button1) }

        tourGuide = TourGuide.create(this) {
            toolTip {
                title { "Expensive Item" }
                description { "Click 'purchase' only when you are ready\nClick on the anywhere to dismiss" }
            }
            overlay {
                disableClickThroughHole { true }
                onClickListener { View.OnClickListener { tourGuide.cleanUp() } }
            }
        }.playOn(button1)
        button2.setOnClickListener { tourGuide.playOn(button1) }
    }
}
