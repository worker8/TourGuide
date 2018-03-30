package tourguide.tourguidedemo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_recycler_view.*
import tourguide.tourguide.TourGuide
import tourguide.tourguidedemo.recyclerview.RecyclerViewAdapter

class RecyclerViewActivity : AppCompatActivity() {
    private val linearLayoutManager = LinearLayoutManager(this)

    private val tourGuide = TourGuide.create(this) {
        overlay {
            backgroundColor = Color.parseColor("#66FF0000")
        }
    }

    private val launchTourGuide = { view: View ->
        tourGuide.playOn(view)
        Unit
    }
    val dismissTourGuide = {
        tourGuide.cleanUp()
        Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        recyclerView.apply {
            adapter = RecyclerViewAdapter(launchTourGuide, dismissTourGuide)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = linearLayoutManager
        }
    }
}