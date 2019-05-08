package tourguide.tourguidedemo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_basic.*
import tourguide.tourguide.Config
import tourguide.tourguide.Pointer
import tourguide.tourguide.TourGuide
import tourguide.tourguide.ViewHole

class BasicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        /* Get parameters from main activity */
        val colorDemo = intent.getBooleanExtra(COLOR_DEMO, false)
        val gravityDemo = intent.getBooleanExtra(GRAVITY_DEMO, false)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)

        // the return handler is used to manipulate the cleanup of all the tutorial elements
        val tourGuide = TourGuide.create(this) {
            overlay {
                backgroundColor { Color.parseColor("#66FF0000") }
            }
        }
        val button1Hole = ViewHole(button1, Config().apply {
            shape { Config.Shape.Circle() }
            pointer {
                technique { Pointer.Technique.CLICK }
                color {
                    if (colorDemo) {
                        Color.RED
                    } else {
                        Color.WHITE
                    }
                }
                gravity {
                    if (gravityDemo) {
                        Gravity.BOTTOM or Gravity.END
                    } else {
                        Gravity.CENTER
                    }
                }
            }
            toolTip {
                title { "Welcome!" }
                description { "Click on Get Started to begin..." }
            }
        })
        val handler = tourGuide.playOn(button1Hole).show()

        button1.setOnClickListener { handler.cleanUp() }
        button2.setOnClickListener {
            handler.playOn(button1Hole).show()
        }
    }

    companion object {
        const val COLOR_DEMO = "color_demo"
        const val GRAVITY_DEMO = "gravity_demo"
    }
}
