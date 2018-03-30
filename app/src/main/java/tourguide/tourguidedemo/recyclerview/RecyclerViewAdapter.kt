package tourguide.tourguidedemo.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_recycler_view.view.*
import tourguide.tourguidedemo.R

class RecyclerViewAdapter(val launchTourGuide: (View) -> Unit, val dismissTourGuide: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(_holder: RecyclerView.ViewHolder, position: Int) {
        val holder = _holder as DemoViewHolder
        holder.itemView.rowTextView
                .apply { text = "row #${position}" }
                .also {
                    if (position == 1) {
                        launchTourGuide.invoke(it)
                        holder.itemView.setOnClickListener {
                            dismissTourGuide.invoke()
                        }
                    }
                }
    }

    override fun getItemCount() = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.row_recycler_view, parent, false)
                    .let { DemoViewHolder(it) }

    class DemoViewHolder(item: View) : RecyclerView.ViewHolder(item)

}