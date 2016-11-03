package tourguide.tourguidedemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;


public class AdjustPaddingOverlayActivity extends ActionBarActivity {
    public TourGuide mTutorialHandler;
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_overlay_customization);

        final Button button = (Button) findViewById(R.id.button);
        Button next_button = (Button) findViewById(R.id.next_button);
        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.input_layout);
        final EditText paddingET = (EditText) findViewById(R.id.padding_edit_text);
        paddingET.setText(String.valueOf(10));
        textInputLayout.setVisibility(View.VISIBLE);

        next_button.setVisibility(View.GONE);

        final ToolTip toolTip = new ToolTip()
                .setTitle("Hello!")
                .setDescription(String.format("Current Overlay Padding: %s", paddingET.getText().toString()));

        final Overlay overlay = new Overlay()
                .setBackgroundColor(Color.parseColor("#AAFF0000"))
                // Note: disable click has no effect when setOnClickListener is used, this is here for demo purpose
                // if setOnClickListener is not used, disableClick() will take effect
                .disableClick(false)
                .disableClickThroughHole(false)
                .setStyle(Overlay.Style.RoundedRectangle)
                .setHolePadding(Integer.valueOf(paddingET.getText().toString()))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTutorialHandler.cleanUp();
                    }
                });

        // the return handler is used to manipulate the cleanup of all the tutorial elements
        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(null)
                .setToolTip(toolTip)
                .setOverlay(overlay)
                .playOn(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                mTutorialHandler.playOn(button);
            }
        });
        button.setText("   show   ");

        paddingET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                overlay.setHolePadding((charSequence.length() > 0 && TextUtils.isDigitsOnly(charSequence)) ? Integer.valueOf(charSequence.toString()) : 10);
                toolTip.setDescription(String.format("Current Overlay Padding: %s", charSequence));
                mTutorialHandler.setOverlay(overlay);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
