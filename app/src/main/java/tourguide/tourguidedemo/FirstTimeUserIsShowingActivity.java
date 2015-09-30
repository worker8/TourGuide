package tourguide.tourguidedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;


public class FirstTimeUserIsShowingActivity extends ActionBarActivity {
    public TourGuide mTutorialHandler;
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

        Button button = (Button)findViewById(R.id.button);
        Button checkVisibilityButton = (Button)findViewById(R.id.check_visibility_button);

        ToolTip toolTip = new ToolTip().
                setTitle("Welcome!").
                setDescription("Click on Get Started to begin...")
                .setGravity(Gravity.TOP);

        // Setup pointer for demo
        Pointer pointer = new Pointer();
        if (color_demo) {
            pointer.setColor(Color.RED);
        }
        if (gravity_demo) {
            pointer.setGravity(Gravity.BOTTOM|Gravity.RIGHT);
        }

        // the return handler is used to manipulate the cleanup of all the tutorial elements
        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(pointer)
                .setToolTip(toolTip)
                .setOverlay(new Overlay().setBackgroundColor(Color.parseColor("#66FF0000")).disableClick(false))
                .playOn(button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
            }
        });

        checkVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTutorialHandler.isShowing()) {
                    Toast.makeText(FirstTimeUserIsShowingActivity.this, "TourGuide is Showing", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(FirstTimeUserIsShowingActivity.this, "TourGuide is not showing", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
