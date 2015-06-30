package tourguide.tourguidedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;


public class NoPointerActivity extends ActionBarActivity {
    public TourGuide mTutorialHandler;
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Get parameters from main activity */
        Intent intent = getIntent();

        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_basic);

        Button button = (Button)findViewById(R.id.button);

        ToolTip toolTip = new ToolTip().
                setTitle("Welcome!").
                setDescription("Click on Get Started to begin...");

        // the return handler is used to manipulate the cleanup of all the tutorial elements
        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(null) // set pointer to null
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
