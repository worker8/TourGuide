package tourguide.tourguidedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;

/**
 * {@link OverlaySequenceTourActivity} demonstrates how to use TourGuide in sequence one after another
 *
 * Test it with scenarios below:
 * 1) if the continue method is Overlay, and both of defaultOverlay's OnClicklistener
 *    and Overlay's OnClickListener is set, compiler will throw an error at runtime
 *
 * 2) If the one of the tourGuide is not set, it will follow the settings of default Overlay.
 *
 * 3) Warning message will be shown if both of the overlay's onClickListener is not set
 *
 * 4) If the continueMethod is null and overlay is not set, it will follow the default Overlay method.
 *    If default overlay and overlay are set, it will not throw the runTimeErrorException and it
 *    will follow overlay's method.
 *    .
 */
public class OverlaySequenceTourActivity extends ActionBarActivity {
    public ChainTourGuide mTourGuideHandler;
    public Activity mActivity;
    private Button mButton1, mButton2, mButton3;
    private Animation mEnterAnimation, mExitAnimation;

    public static final int OVERLAY_METHOD = 1;
    public static final int OVERLAY_LISTENER_METHOD = 2;

    public static final String CONTINUE_METHOD = "continue_method";
    private int mChosenContinueMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mChosenContinueMethod = intent.getIntExtra(CONTINUE_METHOD, OVERLAY_METHOD);

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

        if (mChosenContinueMethod == OVERLAY_METHOD) {
            runOverlay_ContinueMethod();
        } else if (mChosenContinueMethod == OVERLAY_LISTENER_METHOD){
            runOverlayListener_ContinueMethod();
        }
    }

    private void runOverlay_ContinueMethod(){
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        ChainTourGuide tourGuide1 = ChainTourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("ContinueMethod.Overlay")
                                               .setDescription("When using this ContinueMethod, you can't specify the additional action before going to next TourGuide.")
                                               .setGravity(Gravity.BOTTOM)
                                          )
                               // note that there is not Overlay here, so the default one will be used
                               .playLater(mButton1);

        ChainTourGuide tourGuide2 = ChainTourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("Tip")
                                               .setDescription("Individual Overlay will be used when it's supplied.")
                                               .setGravity(Gravity.BOTTOM | Gravity.LEFT)
                                               .setBackgroundColor(Color.parseColor("#c0392b"))
                                          )
                               .setOverlay(new Overlay()
                                               .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                                               .setEnterAnimation(mEnterAnimation)
                                               .setExitAnimation(mExitAnimation)
                                          )
                               .playLater(mButton2);

        ChainTourGuide tourGuide3 = ChainTourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("ContinueMethod.Overlay")
                                               .setDescription("When using this ContinueMethod, you don't need to call tourGuide.next() explicitly, TourGuide will do it for you.")
                                               .setGravity(Gravity.TOP)
                                          )
                               // note that there is not Overlay here, so the default one will be used
                               .playLater(mButton3);

        Sequence sequence = new Sequence.SequenceBuilder()
                                .add(tourGuide1, tourGuide2, tourGuide3)
                                .setDefaultOverlay(new Overlay()
                                                       .setEnterAnimation(mEnterAnimation)
                                                       .setExitAnimation(mExitAnimation)
                                                  )
                                .setDefaultPointer(null)
                                .setContinueMethod(Sequence.ContinueMethod.Overlay)
                                .build();


        ChainTourGuide.init(this).playInSequence(sequence);
    }

    private void runOverlayListener_ContinueMethod(){
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        ChainTourGuide tourGuide1 = ChainTourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("ContinueMethod.OverlayListener")
                                               .setDescription("When using OverlayListener, you can add more actions before proceeding to next TourGuide, such as showing a Toast message.")
                                               .setGravity(Gravity.BOTTOM)
                                          )
                               // note that there is not Overlay here, so the default one will be used
                               .playLater(mButton1);

        ChainTourGuide tourGuide2 = ChainTourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("Tip")
                                               .setDescription("Individual Overlay will be used when it's supplied.")
                                               .setBackgroundColor(Color.parseColor("#c0392b"))
                                               .setGravity(Gravity.BOTTOM | Gravity.LEFT)
                                          )
                               .setOverlay(new Overlay()
                                               .setEnterAnimation(mEnterAnimation)
                                               .setExitAnimation(mExitAnimation)
                                               .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                                               .setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       mTourGuideHandler.next();
                                                   }
                                               })
                                          )
                               .playLater(mButton2);

        ChainTourGuide tourGuide3 = ChainTourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("ContinueMethod.OverlayListener")
                                               .setDescription("When using this ContinueMethod, you need to call tourGuide.next() explicitly.")
                                               .setGravity(Gravity.TOP)
                               )
                               // note that there is not Overlay here, so the default one will be used
                               .playLater(mButton3);

        Sequence sequence = new Sequence.SequenceBuilder()
                                .add(tourGuide1, tourGuide2, tourGuide3)
                                .setDefaultOverlay(new Overlay()
                                                       .setEnterAnimation(mEnterAnimation)
                                                       .setExitAnimation(mExitAnimation)
                                                       .setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               Toast.makeText(OverlaySequenceTourActivity.this, "default Overlay clicked", Toast.LENGTH_SHORT).show();
                                                               mTourGuideHandler.next();
                                                           }
                                                       })
                                                  )
                                .setDefaultPointer(null)
                                .setContinueMethod(Sequence.ContinueMethod.OverlayListener)
                                .build();

        mTourGuideHandler = ChainTourGuide.init(this).playInSequence(sequence);
    }
}
