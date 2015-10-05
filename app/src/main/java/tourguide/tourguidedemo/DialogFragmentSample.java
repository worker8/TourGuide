package tourguide.tourguidedemo;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * Created by aaronliew on 8/27/15.
 */
public class DialogFragmentSample extends DialogFragment {
    public TourGuide mTutorialHandler,mTutorialHandler1,mTutorialHandler2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_basic, container);
        getDialog().setTitle("Bello!");

        Button button = (Button) view.findViewById(R.id.button);
        final ImageButton closebutton = (ImageButton) view.findViewById(R.id.cross);
        final ImageButton gmailbutton = (ImageButton) view.findViewById(R.id.gmail);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.tourguide);
//
        final ToolTip toolTip = new ToolTip().
                setTitle("Welcome!").
                setDescription("Click on Get Started to begin...");

        // Setup pointer for demo
        final Pointer pointer = new Pointer();


        // the return handler is used to manipulate the cleanup of all the tutorial elements
        mTutorialHandler = TourGuide.init(getActivity()).with(TourGuide.Technique.Click)
                .setPointer(pointer)
                .setToolTip(toolTip)
                .setOverlay(new Overlay().setBackgroundColor(Color.parseColor("#66FF0000")))
                .playOn(button);


        mTutorialHandler1 = TourGuide.init(getActivity()).with(TourGuide.Technique.Click)
                .setPointer(pointer)
                .setToolTip(toolTip)
                .setOverlay(new Overlay().setBackgroundColor(Color.parseColor("#66FF0000")))
                .playOn(closebutton);

        mTutorialHandler2 = TourGuide.init(getActivity()).with(TourGuide.Technique.Click)
                .setPointer(pointer)
                .setToolTip(toolTip)
                .setOverlay(new Overlay().setBackgroundColor(Color.parseColor("#66FF0000")))
                .playOn(gmailbutton);



        getDialog().setTitle("hihi Sir!");

//        mTutorialHandler.contentArea = frameLayout;
//        mTutorialHandler.mContainer = frameLayout;


        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTutorialHandler1.cleanUp();
            }
        });

        gmailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTutorialHandler2.cleanUp();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTutorialHandler.cleanUp();
                dismissAllowingStateLoss();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setLayout(1000, 1000);
//        window.setGravity(Gravity.CENTER);
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}