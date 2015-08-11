package tourguide.instrumentation.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;
import tourguide.tourguidedemo.R;


public class ToolTipMeasureTestActivity extends ActionBarActivity {
    public TourGuide mTutorialHandler;
    public Activity mActivity;
    public static final String TOOLTIP_NUM = "tooltip_num";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Get parameters from main activity */
        Intent intent = getIntent();
        int tooltip_num = intent.getIntExtra(TOOLTIP_NUM, 1);

        super.onCreate(savedInstanceState);
        mActivity = this;
        int gravity;

        if (tooltip_num == 1) {
            setContentView(R.layout.activity_tooltip_gravity_i);
            gravity = Gravity.RIGHT | Gravity.BOTTOM;
        } else if (tooltip_num == 2) {
            setContentView(R.layout.activity_tooltip_gravity_ii);
            gravity = Gravity.LEFT | Gravity.BOTTOM;
        } else if (tooltip_num == 3) {
            setContentView(R.layout.activity_tooltip_gravity_iii);
            gravity = Gravity.LEFT | Gravity.TOP;
        } else {
            setContentView(R.layout.activity_tooltip_gravity_iv);
            gravity = Gravity.RIGHT | Gravity.TOP;
        }
        Button button = (Button)findViewById(R.id.button);

        ToolTip toolTip = new ToolTip().
                setTitle("Welcome!").
                setDescription("This is a really really long title....This is a really really long title....This is a really really long title....This is a really really long title....This is a really really long title....This is a really really long title....This is a really really long title....").
                setBackgroundColor(Color.parseColor("#2980b9")).
                setTextColor(Color.parseColor("#FFFFFF")).
                setGravity(gravity).
                setShadow(true);

        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(toolTip)
                .setOverlay(new Overlay())
                .playOn(button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
            }
        });
    }
}
