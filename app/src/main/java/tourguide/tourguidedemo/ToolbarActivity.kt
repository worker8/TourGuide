package tourguide.tourguidedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_toolbar.*
import tourguide.tourguide.Overlay
import tourguide.tourguide.Pointer
import tourguide.tourguide.ToolTip
import tourguide.tourguide.TourGuide


class ToolbarActivity : AppCompatActivity() {

    private val hasStatusBar
        get() = intent.getBooleanExtra(STATUS_BAR, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        /* Get parameters from main activity */
        if (!hasStatusBar) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu
        menuInflater.inflate(R.menu.menu_demo_main, menu)

        // We need to get the menu item as a View in order to work with TourGuide
        val menuItem = menu.getItem(0)
        val button = menuItem.actionView as ImageView

        // just adding some padding to look better
        val density = resources.displayMetrics.density
        val padding = (5 * density).toInt()
        button.setPadding(padding, padding, padding, padding)

        // set an image
        button.setImageDrawable(resources.getDrawable(android.R.drawable.ic_dialog_email))

        val toolTip = ToolTip()
                .setTitle("Welcome!")
                .setDescription("Click on Get Started to begin...")
                .setGravity(Gravity.LEFT or Gravity.BOTTOM)

        val tourGuide = TourGuide.init(this).with(TourGuide.Technique.CLICK)
                .motionType(TourGuide.MotionType.CLICK_ONLY)
                .setPointer(Pointer())
                .setToolTip(toolTip)
                .apply {
                    overlay { Overlay() }
                }.playOn(button)
        button.setOnClickListener { tourGuide.cleanUp() }

        return true
    }

    companion object {
        val STATUS_BAR = "status_bar"
    }
}
