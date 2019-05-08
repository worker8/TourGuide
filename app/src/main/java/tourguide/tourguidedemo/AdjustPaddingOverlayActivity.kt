package tourguide.tourguidedemo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.activity_overlay_customization.*
import tourguide.tourguide.Config
import tourguide.tourguide.TourGuide


class AdjustPaddingOverlayActivity : AppCompatActivity() {
    lateinit var tourGuide: TourGuide
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overlay_customization)
        tourGuide = TourGuide
                .create(this) {
                    overlay {
                        disableClick { false }
                        backgroundColor { Color.parseColor("#AAFF0000") }
                        onClickListener { View.OnClickListener { tourGuide.cleanUp() } }
                    }
                }.playOn(button) {
                    toolTip {
                        title { "Hello!" }
                        description { String.format("Current OVERLAY Padding: %s", paddingEditText.text.toString()) }
                    }
                    canClickThroughHole { true }
                    shape { Config.Shape.RoundedRectangle(padding = Integer.valueOf(paddingEditText.text.toString())) }
                }.show()

        paddingEditText.setText(10.toString())
        textInputLayout.visibility = View.VISIBLE

        nextButton.visibility = View.GONE

        button.setOnClickListener {
            tourGuide.cleanUp()
            tourGuide.playOn(button) {
                toolTip {
                    title { "Hello!" }
                    description { String.format("Current OVERLAY Padding: %s", paddingEditText.text.toString()) }
                }
                canClickThroughHole { true }
                shape { Config.Shape.RoundedRectangle(padding = Integer.valueOf(paddingEditText.text.toString())) }
            }.show()
        }
        button.text = "   show   "

        paddingEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })
    }
}
