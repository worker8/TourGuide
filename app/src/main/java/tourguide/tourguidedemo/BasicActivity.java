package tourguide.tourguidedemo;

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


public class BasicActivity extends ActionBarActivity {
    public TourGuide mTourGuideHandler;
    public Activity mActivity;
    public static final String COLOR_DEMO = "color_demo";
    public static final String GRAVITY_DEMO = "gravity_demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Get parameters from main activity */
        Intent intent = getIntent();
        boolean color_demo = intent.getBooleanExtra(COLOR_DEMO, false);
        boolean gravity_demo = intent.getBooleanExtra(GRAVITY_DEMO, false);

        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_basic);

        final Button button1 = (Button)findViewById(R.id.button1);
        final Button button2 = (Button)findViewById(R.id.button2);

        ToolTip toolTip = new ToolTip().
                setTitle("Welcome!").
                setDescription("Click on Get Started to begin...");

        // Setup pointer for demo
        Pointer pointer = new Pointer();
        if (color_demo) {
            pointer.setColor(Color.RED);
        }
        if (gravity_demo) {
            pointer.setGravity(Gravity.BOTTOM|Gravity.RIGHT);
            button1.setText("BUTTON\n THAT IS\n PRETTY BIG");
        }

        // the return handler is used to manipulate the cleanup of all the tutorial elements
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(pointer)
                .setToolTip(toolTip)
                .setOverlay(new Overlay().setBackgroundColor(Color.parseColor("#66FF0000")))
                .playOn(button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTourGuideHandler != null) {
                    mTourGuideHandler.cleanUp();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTourGuideHandler.playOn(button1);
            }
        });
    }
}
