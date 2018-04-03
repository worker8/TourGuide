package tourguide.tourguidedemo.main

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row.view.*
import tourguide.tourguidedemo.*

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val numOfRow = 20
    private fun View.startActivity(intent: Intent) = ContextCompat.startActivity(context, intent, null)

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            decorateBackground(this, position)

            when (position) {
                0 -> {
                    textView.text = "Basic Activity"
                    setOnClickListener {
                        startActivity(Intent(context, BasicActivity::class.java))
                    }
                }
                1 -> {
                    textView.text = "Pointer: color"
                    setOnClickListener {
                        startActivity(Intent(context, BasicActivity::class.java).apply {
                            putExtra(BasicActivity.COLOR_DEMO, true)
                        })
                    }
                }
                2 -> {
                    textView.text = "Pointer: gravity"
                    setOnClickListener {
                        startActivity(Intent(context, BasicActivity::class.java).apply {
                            putExtra(BasicActivity.GRAVITY_DEMO, true)
                        })
                    }
                }
                3 -> {
                    textView.text = "Toolbar Example"
                    setOnClickListener {
                        startActivity(Intent(context, ToolbarActivity::class.java).apply {
                            putExtra(ToolbarActivity.STATUS_BAR, true)
                        })
                    }
                }
                4 -> {
                    textView.text = "Toolbar Example\n(no status bar)"
                    setOnClickListener {
                        startActivity(Intent(context, ToolbarActivity::class.java).apply {
                            putExtra(ToolbarActivity.STATUS_BAR, false)
                        })
                    }
                }
                5 -> {
                    textView.text = "ToolTip Gravity I"
                    setOnClickListener {
                        startActivity(Intent(context, ToolTipGravityActivity::class.java).apply {
                            putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 1)
                        })
                    }
                }
                6 -> {
                    textView.text = "ToolTip Gravity II"
                    setOnClickListener {


                        startActivity(Intent(context, ToolTipGravityActivity::class.java).apply {
                            putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 2)

                        })
                    }
                }
                7 -> {
                    textView.text = "ToolTip Gravity III"
                    setOnClickListener {
                        startActivity(Intent(context, ToolTipGravityActivity::class.java).apply {
                            putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 3)
                        })
                    }
                }
                8 -> {
                    textView.text = "ToolTip Gravity IV"
                    setOnClickListener {

                        startActivity(Intent(context, ToolTipGravityActivity::class.java).apply {
                            putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 4)
                        })
                    }
                }
                9 -> {
                    textView.text = "ToolTip Customization"
                    setOnClickListener {
                        startActivity(Intent(context, ToolTipCustomizationActivity::class.java))
                    }
                }
                10 -> {
                    textView.text = "Multiple ToolTip"
                    setOnClickListener {
                        startActivity(Intent(context, MultipleToolTipActivity::class.java))
                    }
                }
                11 -> {
                    textView.text = "Overlay Customization"
                    setOnClickListener {
                        startActivity(Intent(context, OverlayCustomizationActivity::class.java))
                    }
                }
                12 -> {
                    textView.text = "ToolTip & Overlay only, no Pointer"
                    setOnClickListener {
                        startActivity(Intent(context, NoPointerActivity::class.java))
                    }
                }
                13 -> {
                    textView.text = "Overlay only, no Tooltip, no Pointer"
                    setOnClickListener {
                        startActivity(Intent(context, NoPointerNoToolTipActivity::class.java))
                    }
                }
                14 -> {
                    textView.text = "ToolTip & Pointer only, no Overlay"
                    setOnClickListener {
                        startActivity(Intent(context, NoOverlayActivity::class.java))
                    }
                }
                15 -> {
                    textView.text = "Button Tour (Manual)"
                    infoIcon.visibility = View.VISIBLE
                    infoIcon.setOnClickListener {
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Button Tour").setMessage("- Button Tour example shows a sequence of TourGuide running on different buttons. \n- The method of proceeding to the next TourGuide is to press on the button itself. \n- This is suitable when you actually want the user to click on the button during the Tour.\n")
                        builder.create().show()
                    }
                    setOnClickListener {
                        startActivity(Intent(context, ManualSequenceActivity::class.java))
                    }
                }
                16 -> {
                    textView.text = "Navigational Drawer"
                    setOnClickListener {
                        startActivity(Intent(context, NavDrawerActivity::class.java))
                    }
                }
                17 -> {
                    textView.text = "Rounded Rectangle Overlay"
                    setOnClickListener {
                        startActivity(Intent(context, RoundedRectangleOverlayActivity::class.java))
                    }
                }
                18 -> {
                    textView.text = "Adjust Overlay Padding"
                    setOnClickListener {
                        startActivity(Intent(context, AdjustPaddingOverlayActivity::class.java))
                    }
                }
                else -> {
                    textView.text = "Recycler View"
                    setOnClickListener {
                        startActivity(Intent(context, RecyclerViewActivity::class.java))
                    }
                }
            }
        }
    }

    private fun decorateBackground(view: View, position: Int) =
            view.apply {
                setBackgroundColor(ContextCompat.getColor(context,
                        if (position % 2 == 0) {
                            R.color.blue1
                        } else {
                            R.color.blue2
                        }))
            }


    override fun getItemCount() = numOfRow
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder((LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.row, parent, false)))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
