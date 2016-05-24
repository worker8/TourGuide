package tourguide.tourguidedemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.TourGuide;


public class NoPointerNoToolTipActivity extends ActionBarActivity {
    public TourGuide mTourGuideHandler;
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_basic);

        final Button button1 = (Button) findViewById(R.id.button1);
        final Button button2 = (Button) findViewById(R.id.button2);
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(null) // set pointer to null
                .setToolTip(null)
                .setOverlay(new Overlay())
                .playOn(button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTourGuideHandler.cleanUp();
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
