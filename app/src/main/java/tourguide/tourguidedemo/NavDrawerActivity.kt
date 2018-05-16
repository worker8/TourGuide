package tourguide.tourguidedemo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_nav_drawer.*
import tourguide.tourguide.TourGuide

class NavDrawerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        /* setup toolbar */
        setSupportActionBar(toolbar.apply { title = "Nav Drawer Example" })
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        val tourGuide = TourGuide.create(this) {
            pointer { }
            overlay { backgroundColor { Color.parseColor("#66FF0000") } }
            toolTip {
                title { "" }
                description { "hello world" }
            }
        }

        val drawerToggle = object : ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open_string, R.string.drawer_close_string) {
            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
                /* We need call playOn only after the drawer is opened,
                   so that TourGuide knows the updated location of the targetted view */
                tourGuide.playOn(textView1)
            }
        }
        drawerLayout.setDrawerListener(drawerToggle)
        drawerToggle.syncState()

        /* setup clean up code */
        textView1.setOnClickListener {
            tourGuide.cleanUp()
            drawerLayout.closeDrawers()
        }

        textView1.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                textView1.viewTreeObserver.removeGlobalOnLayoutListener(this)// make sure this only run once
                drawerLayout.openDrawer(Gravity.LEFT)
            }
        })
    }

}
