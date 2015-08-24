package tourguide.tourguidedemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import tourguide.tourguide.ContinueMethod;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * InSequenceActivity demonstrates how to use TourGuide in sequence one after another
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
public class InSequenceWithContinueMethodActivity extends ActionBarActivity {
    public TourGuide mTutorialHandler;
    public Activity mActivity;
    private Button mButton1, mButton2, mButton3;
    private Animation mEnterAnimation, mExitAnimation;

    private int OVERLAY_METHOD = 1;
    private int OVERLAY_LISTENER_METHOD = 2;
    private int TOOLTIP_METHOD = 3;
    private int TOOLTIP_LISTENER_METHOD = 4;
    private int mChosenContinueMethod = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        } else if (mChosenContinueMethod == TOOLTIP_METHOD){
            runTooltip_ContinueMethod();
        } else if (mChosenContinueMethod == TOOLTIP_LISTENER_METHOD){
            runTooltipListener_ContinueMethod();
        }
    }

    private void runOverlay_ContinueMethod(){
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        TourGuide tourGuide1 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("ToolTip #1")
                                               .setDescription("ToolTip #1 Description")
                                               .setGravity(Gravity.RIGHT)
                                          )
                               .playLater(mButton1);

        TourGuide tourGuide2 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("ToolTip #2")
                                               .setDescription("ToolTip #2 has a custom Overlay, individual Overlay will be prioritized over the default Overlay")
                                               .setGravity(Gravity.BOTTOM | Gravity.LEFT)
                                               .setBackgroundColor(Color.parseColor("#ffe74c3c"))
                               )
                               .setOverlay(new Overlay()
                                               .setBackgroundColor(Color.parseColor("#550000FF"))
                                               .setEnterAnimation(mEnterAnimation)
                                               .setExitAnimation(mExitAnimation)
                                          )
                               .playLater(mButton2);

        TourGuide tourGuide3 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("Hey!")
                                               .setDescription("It's time to say goodbye")
                                               .setGravity(Gravity.TOP | Gravity.RIGHT)
                                          )
                               .playLater(mButton3);

        Sequence sequence = new Sequence.SequenceBuilder()
                                .add(tourGuide1, tourGuide2, tourGuide3)
                                .setDefaultOverlay(new Overlay()
                                                       .setEnterAnimation(mEnterAnimation)
                                                       .setExitAnimation(mExitAnimation)
                                                  )
                                .setDefaultPointer(new Pointer())
                                .setContinueMethod(ContinueMethod.Overlay)
                                .build();


        mTutorialHandler = TourGuide.init(this).playInSequence(sequence);
    }

    private void runOverlayListener_ContinueMethod(){
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        TourGuide tourGuide1 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("ToolTip #1")
                                               .setDescription("ContinueMethod is OverlayListener")
                                               .setGravity(Gravity.RIGHT)
                                          )
                               .playLater(mButton1);

        TourGuide tourGuide2 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("ToolTip #2")
                                               .setDescription("You need to either set the default listener or set al individual TourGuide's listener")
                                               .setGravity(Gravity.BOTTOM | Gravity.LEFT)
                                          )
                               .setOverlay(new Overlay()
                                               .setEnterAnimation(mEnterAnimation)
                                               .setExitAnimation(mExitAnimation)
                                               .setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       Toast.makeText(mActivity, "Individual Overlay override the default ones! :)", Toast.LENGTH_LONG).show();
                                                       mTutorialHandler.next();
                                                   }
                                               })
                                          )
                               .playLater(mButton2);

        TourGuide tourGuide3 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                       .setTitle("Hey!")
                                       .setDescription("It's time to say goodbye")
                                       .setGravity(Gravity.TOP | Gravity.RIGHT))
                               .playLater(mButton3);

        Sequence sequence = new Sequence.SequenceBuilder()
                                .add(tourGuide1, tourGuide2, tourGuide3)
                                .setDefaultOverlay(new Overlay()
                                                       .setEnterAnimation(mEnterAnimation)
                                                       .setExitAnimation(mExitAnimation)
                                                       .setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               Toast.makeText(InSequenceWithContinueMethodActivity.this, "Bello! This is the default Overlay", Toast.LENGTH_SHORT).show();
                                                               mTutorialHandler.next();
                                                           }
                                                       })
                                                  )
                                .setDefaultPointer(new Pointer())
                                .setContinueMethod(ContinueMethod.OverlayListener)
                                .build();

        mTutorialHandler = TourGuide.init(this).playInSequence(sequence);
    }

    private void runTooltip_ContinueMethod(){
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        TourGuide tourGuide1 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("Hey!")
                                               .setDescription("I'm the top fellow")
                                               .setGravity(Gravity.RIGHT)
                                               .setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Toast.makeText(InSequenceWithContinueMethodActivity.this, "Bello! This is the top fellow's tooltip", Toast.LENGTH_SHORT).show();
                                                    }
                                               })
                                          )
                               .playLater(mButton1);

        TourGuide tourGuide2 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("Hey!")
                                               .setDescription("Just the middle man")
                                               .setGravity(Gravity.BOTTOM | Gravity.LEFT)
                                          )
                               .setOverlay(new Overlay()
                                               .setEnterAnimation(mEnterAnimation)
                                               .setExitAnimation(mExitAnimation)
                                          )
                               .playLater(mButton2);

        TourGuide tourGuide3 = TourGuide.init(this).playLater(mButton3);

        Sequence sequence = new Sequence.SequenceBuilder()
                                .add(tourGuide1, tourGuide2, tourGuide3)
                                .setDefaultOverlay(new Overlay()
                                                       .setEnterAnimation(mEnterAnimation).setExitAnimation(mExitAnimation)
                                                       .setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               Toast.makeText(InSequenceWithContinueMethodActivity.this, "Bello! This is the default Overlay", Toast.LENGTH_SHORT).show();
                                                               mTutorialHandler.next();
                                                           }
                                                       })
                                                  )
                                .setDefaultToolTip(new ToolTip()
                                                       .setTitle("Hey!")
                                                       .setDescription("It's time to say goodbye")
                                                       .setGravity(Gravity.TOP | Gravity.RIGHT)
                                                       .setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               Toast.makeText(InSequenceWithContinueMethodActivity.this, "Bello! This is the default Tooltip Listener", Toast.LENGTH_SHORT).show();
                                                               mTutorialHandler.next();
                                                           }
                                                       })
                                                  )
                                .setDefaultPointer(new Pointer())
                                .setContinueMethod(ContinueMethod.ToolTip)
                                .build();

        mTutorialHandler = TourGuide.init(this).playInSequence(sequence);
    }

    private void runTooltipListener_ContinueMethod(){
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        TourGuide tourGuide1 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("Hey!")
                                               .setDescription("I'm the top fellow")
                                               .setGravity(Gravity.RIGHT)
                                          )
                               .playLater(mButton1);

        TourGuide tourGuide2 = TourGuide.init(this)
                               .setToolTip(new ToolTip()
                                               .setTitle("Hey!")
                                               .setDescription("Just the middle man")
                                               .setGravity(Gravity.BOTTOM | Gravity.LEFT)
                                          )
                               .setOverlay(new Overlay()
                                               .setEnterAnimation(mEnterAnimation)
                                               .setExitAnimation(mExitAnimation)
                                          )
                               .playLater(mButton2);

        TourGuide tourGuide3 = TourGuide.init(this).playLater(mButton3);

        Sequence sequence = new Sequence.SequenceBuilder()
                                .add(tourGuide1, tourGuide2, tourGuide3)
                                .setDefaultOverlay(new Overlay()
                                                       .setEnterAnimation(mEnterAnimation).setExitAnimation(mExitAnimation)
                                                       .setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               Toast.makeText(InSequenceWithContinueMethodActivity.this, "Bello! This is the default Overlay", Toast.LENGTH_SHORT).show();
                                                               mTutorialHandler.next();
                                                           }
                                                       })
                                                  )
                                .setDefaultToolTip(new ToolTip()
                                                       .setTitle("Hey!")
                                                       .setDescription("It's time to say goodbye")
                                                       .setGravity(Gravity.TOP | Gravity.RIGHT)
                                                       .setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               Toast.makeText(InSequenceWithContinueMethodActivity.this, "Bello! This is the default Tooltip Listener", Toast.LENGTH_SHORT).show();
                                                               mTutorialHandler.next();
                                                           }
                                                       })
                                                  )
                                .setDefaultPointer(new Pointer())
                                .setContinueMethod(ContinueMethod.ToolTipListener)
                                .build();

        mTutorialHandler = TourGuide.init(this).playInSequence(sequence);
    }
}
