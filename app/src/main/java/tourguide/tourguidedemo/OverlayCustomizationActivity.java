package tourguide.tourguidedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;


public class OverlayCustomizationActivity extends ActionBarActivity {
    public TourGuide mTutorialHandler;
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_overlay_customization);

        Button button = (Button)findViewById(R.id.button);
        Button next_button = (Button)findViewById(R.id.next_button);

        ToolTip toolTip = new ToolTip().
                setTitle("Hello!").
                setDescription("Click to view tutorial. Next button is disabled until tutorial is viewed");

        Overlay overlay = new Overlay()
                .setBackgroundColor(Color.parseColor("#AAFF0000"))
                // Note: disable click has no effect when setOnClickListener is used, this is here for demo purpose
                // if setOnClickListener is not used, disableClick() will take effect
                .disableClick(false)
                .disableClickThroughHole(false)
                .setStyle(Overlay.Style.Rectangle)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTutorialHandler.cleanUp();
                    }
                });

        // the return handler is used to manipulate the cleanup of all the tutorial elements
        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(toolTip)
                .setOverlay(overlay)
                .playOn(button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "BOOM!", Toast.LENGTH_LONG).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
            }
        });
    }
}
