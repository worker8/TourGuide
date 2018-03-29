package tourguide.tourguidedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_basic.*
import tourguide.tourguide.Pointer
import tourguide.tourguide.ToolTip
import tourguide.tourguide.TourGuide


class NoOverlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)

        val tourGuide = TourGuide.init(this).with(TourGuide.Technique.CLICK)
                .setPointer(Pointer()) // set pointer to null
                .setToolTip(ToolTip().setTitle("Welcome :)").setDescription("Have a nice and fun day!"))
                .playOn(button1)

        button1.setOnClickListener { tourGuide.cleanUp() }
        button2.setOnClickListener { tourGuide.playOn(button1) }
    }
}
