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
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import tourguide.tourguide.AnimateTutorial;
import tourguide.tourguide.ToolTip;


public class ToolbarActivity extends ActionBarActivity {
    public AnimateTutorial mTutorialHandler;
    public Activity mActivity;
    public static final String STATUS_BAR = "status_bar";
    public static final String DISABLE_CLICK = "disable_click";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Get parameters from main activity */
        Intent intent = getIntent();
        boolean status_bar = intent.getBooleanExtra(STATUS_BAR,false);
        boolean disable_click = intent.getBooleanExtra(DISABLE_CLICK,true);
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
        Button button_dont_touch = (Button)findViewById(R.id.button_dont_touch);

        /* default */

        // setup animation
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f, 0F, 200f, 0f);
        translation.setDuration(1000);
        translation.setFillAfter(true);
        translation.setInterpolator(new BounceInterpolator());

        ToolTip toolTip = new ToolTip().
                title("Welcome!").
                description("Click on Get Started to begin...").
                backgroundColor(Color.parseColor("#3498db")).
                textColor(Color.parseColor("#FFFFFF")).
//                enterAnimation(translation).
                exitAnimation(null).
                shadow(true);

        mTutorialHandler = AnimateTutorial.init(this).with(AnimateTutorial.Technique.Click)
                .duration(700)
                .disableClick(disable_click)
                .gravity(Gravity.CENTER)
                .motionType(AnimateTutorial.MotionType.ClickOnly)
                .toolTip(toolTip)
                .playOn(button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
            }
        });
        if(!disable_click) {
            button_dont_touch.setText("User can click this button while TourGuide is showing");
        }
        button_dont_touch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "Booom!",Toast.LENGTH_LONG).show();
            }
        });

    }
}
