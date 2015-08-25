package tourguide.instrumentation.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;
import tourguide.tourguidedemo.R;

public class SequenceOverlayCMTestActivity extends ActionBarActivity {
    public TourGuide mSequenceManagerTG;
    public Activity mActivity;
    private Button mButton1, mButton2, mButton3;
    private Animation mEnterAnimation, mExitAnimation;

    public TourGuide mOverlayTG1, mOverlayTG2, mOverlayTG3;
    public Overlay mDefaultOverlay;

    public static final String TEST_EXCEPTION = "TEST_EXCEPTION";
    public boolean mIsTestException;

    public static final String TEST_TITLE1 = "1_cjejin338hnifhwhkjhfjkdsn";
    public static final String TEST_TITLE2 = "2_cjejin3r3r3rhkjhfjkdsn";
    public static final String TEST_TITLE3 = "3_cjejin333r3rhkj3rdsn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mIsTestException = intent.getBooleanExtra(TEST_EXCEPTION, false);
        Log.d("ddw", "mIsTestException: "+mIsTestException);
        mActivity = this;
        setContentView(R.layout.activity_in_sequence);

        /* Get 3 buttons from layout */
        mButton1 = (Button)findViewById(R.id.button);
        mButton2 = (Button)findViewById(R.id.button2);
        mButton3 = (Button)findViewById(R.id.button3);

        /* setup enter and exit animation */
        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(600);
        mEnterAnimation.setFillAfter(true);

        mExitAnimation = new AlphaAnimation(1f, 0f);
        mExitAnimation.setDuration(600);
        mExitAnimation.setFillAfter(true);

        runOverlay_ContinueMethod();
    }

    private void runOverlay_ContinueMethod(){
        mDefaultOverlay = new Overlay()
                              .setEnterAnimation(mEnterAnimation)
                              .setExitAnimation(mExitAnimation);
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        mOverlayTG1 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle(TEST_TITLE1)
                                               .setDescription("1")
                                               .setGravity(Gravity.BOTTOM)
                               )
                               // note that there is not Overlay here, so the default one will be used
                               .playLater(mButton1);
        Log.d("ddw","mOverlayTG1.getOverlay(): "+mOverlayTG1.getOverlay());
        mOverlayTG2 = TourGuide.init(this)
                .setToolTip(new ToolTip()
                                .setTitle(TEST_TITLE2)
                                .setDescription("2")
                                .setGravity(Gravity.BOTTOM | Gravity.LEFT)
                                .setBackgroundColor(Color.parseColor("#c0392b"))
                           )
                .setOverlay(new Overlay()
                                .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                                .setEnterAnimation(mEnterAnimation)
                                .setExitAnimation(mExitAnimation)
                           )
                .playLater(mButton2);

        mOverlayTG3 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle(TEST_TITLE3)
                                               .setDescription("3")
                                               .setGravity(Gravity.TOP)
                               )
                               // note that there is not Overlay here, so the default one will be used
                               .playLater(mButton3);
        if (mIsTestException){
            mDefaultOverlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do nothing, just for test purpose
                }
            });
        }
        Sequence sequence = new Sequence.SequenceBuilder()
                                .add(mOverlayTG1, mOverlayTG2, mOverlayTG3)
                                .setDefaultOverlay(mDefaultOverlay)
                                .setDefaultPointer(null)
                                .setContinueMethod(Sequence.ContinueMethod.Overlay)
                                .build();

        mSequenceManagerTG = TourGuide.init(this).playInSequence(sequence);
    }
}
