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

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;


public class ToolTipCustomizationActivity extends ActionBarActivity {
    public TourGuide mTutorialHandler;
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_customization);

        Button button = (Button)findViewById(R.id.button);

        Animation animation = new TranslateAnimation(0f, 0f, 200f, 0f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setInterpolator(new BounceInterpolator());

        ToolTip toolTip = new ToolTip()
                            .setTitle("Next Button")
                            .setDescription("Click on Next button to proceed...")
                            .setTextColor(Color.parseColor("#bdc3c7"))
                            .setBackgroundColor(Color.parseColor("#e74c3c"))
                            .setShadow(true)
                            .setGravity(Gravity.TOP | Gravity.LEFT)
                            .setEnterAnimation(animation);


        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setToolTip(toolTip)
                .setOverlay(new Overlay())
                .setPointer(new Pointer())
                .playOn(button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
            }
        });
    }
}
