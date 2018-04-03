package tourguide.tourguidedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.animation.AlphaAnimation
import kotlinx.android.synthetic.main.activity_in_sequence.*
import tourguide.tourguide.Pointer
import tourguide.tourguide.ToolTip
import tourguide.tourguide.TourGuide

/**
 * InSequenceActivity demonstrates how to use TourGuide in sequence one after another
 */
class ManualSequenceActivity : AppCompatActivity() {
    lateinit var tourGuide: TourGuide

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_sequence)

        /* setup enter and exit animation */
        val enterAnimation = AlphaAnimation(0f, 1f)
        enterAnimation.duration = 600
        enterAnimation.fillAfter = true

        val exitAnimation = AlphaAnimation(1f, 0f)
        exitAnimation.duration = 600
        exitAnimation.fillAfter = true

        /* initialize TourGuide without playOn() */
        tourGuide = TourGuide.create(this) {
            pointer { Pointer() }
            toolTip {
                title { "Hey!" }
                description { "I'm the top fellow" }
                gravity { Gravity.RIGHT }
            }
            overlay {
                setEnterAnimation(enterAnimation)
                setExitAnimation(exitAnimation)
            }
        }


//        mTutorialHandler.overlay = Overlay()
//                .setEnterAnimation(enterAnimation)
//                .setExitAnimation(exitAnimation)

        /* setup 1st button, when clicked, cleanUp() and re-run TourGuide on button2 */
        button.setOnClickListener {
            tourGuide.cleanUp()
            tourGuide.setToolTip(ToolTip().setTitle("Hey there!").setDescription("Just the middle man").setGravity(Gravity.BOTTOM or Gravity.LEFT)).playOn(button2)
        }

        /* setup 2nd button, when clicked, cleanUp() and re-run TourGuide on button3 */
        button2.setOnClickListener {
            tourGuide.cleanUp()
            tourGuide.setToolTip(ToolTip().setTitle("Hey...").setDescription("It's time to say goodbye").setGravity(Gravity.TOP or Gravity.RIGHT)).playOn(button3)
        }

        /* setup 3rd button, when clicked, run cleanUp() */
        button3.setOnClickListener { tourGuide.cleanUp() }

        tourGuide.playOn(button)
    }
}
