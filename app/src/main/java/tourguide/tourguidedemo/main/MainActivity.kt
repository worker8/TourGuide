package tourguide.tourguidedemo.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tour_guide_demo_main.*

import tourguide.tourguidedemo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_guide_demo_main)
        mainRecyclerView.adapter = RecyclerViewAdapter()
    }
}
