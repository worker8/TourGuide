package tourguide.tourguidedemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * Note that currently multiple Overlay doesn't work well, but multiple ToolTip is working fine
 * Therefore, if you want to use multiple ToolTip, please switch off the Overlay by .setOverlay(null)
 */
public class MultipleToolTipActivity extends ActionBarActivity {
    public TourGuide mTutorialHandler, mTutorialHandler2;
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        setContentView(R.layout.activity_multiple_tooltip);

        Button button = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.button2);

        // the return handler is used to manipulate the cleanup of all the tutorial elements
        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip()
                            .setTitle("Hey!")
                            .setDescription("I'm the top guy")
                            .setGravity(Gravity.RIGHT))
                .setOverlay(null)
                .playOn(button);
        mTutorialHandler2 = TourGuide.init(mActivity).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip()
                            .setTitle("Hey!")
                            .setDescription("I'm the bottom guy")
                            .setGravity(Gravity.TOP|Gravity.LEFT))
                .setOverlay(null)
                .playOn(button2);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                           }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler2.cleanUp();
            }
        });
    }
}
