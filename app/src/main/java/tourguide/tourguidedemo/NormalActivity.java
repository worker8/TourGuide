package tourguide.tourguidedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tourguide.tourguide.AnimateTutorial;


public class NormalActivity extends ActionBarActivity {
    public AnimateTutorial mTutorialHandler;
    public Activity mActivity;
    public static final String DISABLE_CLICK = "disable_click";
    public static final String IMMERSIVE_MODE = "immersive_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Get parameters from main activity */
        Intent intent = getIntent();
        boolean disable_click = intent.getBooleanExtra(DISABLE_CLICK, true);
        boolean immersive_mode = intent.getBooleanExtra(IMMERSIVE_MODE, false);

        super.onCreate(savedInstanceState);
        if (immersive_mode){
            hideSystemUI();
        }

        mActivity = this;
        setContentView(R.layout.activity_normal);

        Button button = (Button)findViewById(R.id.button);
        Button button_dont_touch = (Button)findViewById(R.id.button_dont_touch);
        mTutorialHandler = AnimateTutorial.init(this).with(AnimateTutorial.Technique.Click)
                .duration(700)
                .disableClick(disable_click)
                .gravity(Gravity.CENTER)
                .motionType(AnimateTutorial.MotionType.ClickOnly)
                .title("Welcome!")
                .description("Click on the start button to begin")
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
    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
