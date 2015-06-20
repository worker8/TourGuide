package tourguide.tourguidedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;


public class CustomizationActivity extends ActionBarActivity {
    public TourGuide mTutorialHandler;
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Get parameters from main activity */
        Intent intent = getIntent();

        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_customization);

        Button button = (Button)findViewById(R.id.button);

        Animation animation = new TranslateAnimation(0f, 0f, 200f, 0f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setInterpolator(new BounceInterpolator());

        ToolTip toolTip = new ToolTip()
                            .title("Next Button")
                            .description("Click on Next button to proceed...")
                            .gravity(Gravity.TOP | Gravity.LEFT)
                            .textColor(Color.parseColor("#bdc3c7"))
                            .backgroundColor(Color.parseColor("#e74c3c"))
                            .enterAnimation(animation)
//                            .padding(10,10,40,10)
                            .shadow(true);
        // divide into 3 section
        // 1. overlay(Overlay)
        // 2. tooltip(ToolTip)
        // 3. pointer(Pointer)

        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .duration(700) //describing バル
                .gravity(Gravity.RIGHT | Gravity.BOTTOM) //describing バル
                .toolTip(toolTip)
                .overlayColor(Color.parseColor("#2c3e50"))
                .overlayStyle(TourGuide.Overlay.Rectangle)
                .playOn(button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
            }
        });
    }
}
