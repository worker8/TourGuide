package tourguide.tourguidedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;


public class ToolbarActivity extends ActionBarActivity {
    public TourGuide mTutorialHandler;
    public Activity mActivity;
    public static final String STATUS_BAR = "status_bar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Get parameters from main activity */
        Intent intent = getIntent();
        boolean status_bar = intent.getBooleanExtra(STATUS_BAR,false);
        if (!status_bar) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        super.onCreate(savedInstanceState);
        mActivity = this;

        setContentView(R.layout.activity_toolbar);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button)findViewById(R.id.button);

        ToolTip toolTip = new ToolTip().
                title("Welcome!").
                description("Click on Get Started to begin...").
                backgroundColor(Color.parseColor("#3498db")).
                textColor(Color.parseColor("#FFFFFF"));

        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .motionType(TourGuide.MotionType.ClickOnly)
                .duration(700)
                .gravity(Gravity.CENTER)
                .toolTip(toolTip)
                .playOn(button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
            }
        });
    }
}
