package tourguide.tourguide;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * TODO: document your custom view class.
 */
public class FrameLayoutWithHole extends FrameLayout {
    private TextPaint mTextPaint;
    private Activity mActivity;
    private TourGuide.MotionType mMotionType;
    private Paint mEraser;

    Bitmap mEraserBitmap;
    private Canvas mEraserCanvas;
    private Paint mPaint;
    private Paint transparentPaint;
    private View mViewHole; // This is the targeted view to be highlighted, where the hole should be placed
    private int mRadius;
    private int [] mPos;
    private float mDensity;
    private Overlay mOverlay;
    public void setViewHole(View viewHole) {
        this.mViewHole = viewHole;
        enforceMotionType();
    }

    private void enforceMotionType(){
        Log.d("ddw","enforceMotionType 1");
        if (mViewHole!=null) {Log.d("ddw","enforceMotionType 2");
            if (mMotionType!=null && mMotionType == TourGuide.MotionType.ClickOnly) {
                Log.d("ddw","enforceMotionType 3");
                Log.d("ddw","only Swiping");
                mViewHole.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        mViewHole.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
            } else if (mMotionType!=null && mMotionType == TourGuide.MotionType.SwipeOnly) {
                Log.d("ddw","enforceMotionType 4");
                Log.d("ddw","only Swiping");
                mViewHole.setClickable(false);
            }
        }
    }

    public FrameLayoutWithHole(Activity context, View view) {
        this(context, view, TourGuide.MotionType.AllowAll);
    }
    public FrameLayoutWithHole(Activity context, View view, TourGuide.MotionType motionType) {
        this(context, view, motionType, new Overlay());
    }

    public FrameLayoutWithHole(Activity context, View view, TourGuide.MotionType motionType, Overlay overlay) {
        super(context);
        mActivity = context;
        mViewHole = view;
        init(null, 0);
        enforceMotionType();
        mOverlay = overlay;

        int [] pos = new int[2];
        mViewHole.getLocationOnScreen(pos);
        mPos = pos;

        mDensity = context.getResources().getDisplayMetrics().density;
        int padding = (int)(20 * mDensity);

        if (mViewHole.getHeight() > mViewHole.getWidth()) {
            mRadius = mViewHole.getHeight()/2 + padding;
        } else {
            mRadius = mViewHole.getWidth()/2 + padding;
        }
        mMotionType = motionType;
    }
    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
//        final TypedArray a = getContext().obtainStyledAttributes(
//                attrs, FrameLayoutWithHole, defStyle, 0);
//
//
//        a.recycle();
        setWillNotDraw(false);
        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        Point size = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(size);

        mEraserBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
        mEraserCanvas = new Canvas(mEraserBitmap);

        mPaint = new Paint();
        mPaint.setColor(0xcc000000);
        transparentPaint = new Paint();
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        mEraser = new Paint();
        mEraser.setColor(0xFFFFFFFF);
        mEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        Log.d("ddw-hole","getHeight: "+ size.y);
        Log.d("ddw-hole","getWidth: " + size.x);

    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
                "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_" ).append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid " ).append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")" );
        }
        sb.append("[" );
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#" ).append(i);
            sb.append("(pid " ).append(event.getPointerId(i));
            sb.append(")=" ).append((int) event.getX(i));
            sb.append("," ).append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";" );
        }
        sb.append("]" );
        Log.d("ddw dump", sb.toString());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //first check if the location button should handle the touch event
        dumpEvent(ev);
        int action = MotionEventCompat.getActionMasked(ev);
        if(mViewHole != null) {
            int[] pos = new int[2];
            mViewHole.getLocationOnScreen(pos);
            Log.d("ddw", "[dispatchTouchEvent] mViewHole.getHeight(): "+mViewHole.getHeight());
            Log.d("ddw", "[dispatchTouchEvent] mViewHole.getWidth(): "+mViewHole.getWidth());

            Log.d("ddw", "[dispatchTouchEvent] Touch X(): "+ev.getRawX());
            Log.d("ddw", "[dispatchTouchEvent] Touch Y(): "+ev.getRawY());

//            Log.d("ddw", "[dispatchTouchEvent] X of image: "+pos[0]);
//            Log.d("ddw", "[dispatchTouchEvent] Y of image: "+pos[1]);

            Log.d("ddw", "[dispatchTouchEvent] X lower bound: "+ pos[0]);
            Log.d("ddw", "[dispatchTouchEvent] X higher bound: "+(pos[0] +mViewHole.getWidth()));

            Log.d("ddw", "[dispatchTouchEvent] Y lower bound: "+ pos[1]);
            Log.d("ddw", "[dispatchTouchEvent] Y higher bound: "+(pos[1] +mViewHole.getHeight()));

            if(ev.getRawY() >= pos[1] && ev.getRawY() <= (pos[1] + mViewHole.getHeight()) && ev.getRawX() >= pos[0] && ev.getRawX() <= (pos[0] + mViewHole.getWidth())) { //location button event
                Log.d("ddw","to the BOTTOM!");
                Log.d("ddwdebug",""+ev.getAction());

//                switch(action) {
//                    case (MotionEvent.ACTION_DOWN) :
//                        Log.d("ddwdebug","Action was DOWN");
//                        return false;
//                    case (MotionEvent.ACTION_MOVE) :
//                        Log.d("ddwdebug","Action was MOVE");
//                        return true;
//                    case (MotionEvent.ACTION_UP) :
//                        Log.d("ddwdebug","Action was UP");
////                        ev.setAction(MotionEvent.ACTION_DOWN|MotionEvent.ACTION_UP);
////                        return super.dispatchTouchEvent(ev);
//                        return false;
//                    case (MotionEvent.ACTION_CANCEL) :
//                        Log.d("ddwdebug","Action was CANCEL");
//                        return true;
//                    case (MotionEvent.ACTION_OUTSIDE) :
//                        Log.d("ddwdebug","Movement occurred outside bounds " +
//                                "of current screen element");
//                        return true;
//                    default :
//                        return super.dispatchTouchEvent(ev);
//                }
//                return mViewHole.onTouchEvent(ev);

                return false;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mEraserBitmap.eraseColor(Color.TRANSPARENT);

        if (mOverlay!=null) {
            mEraserCanvas.drawColor(mOverlay.mBackgroundColor);
            int padding = (int) (10 * mDensity);
            if (mOverlay.mStyle == Overlay.Style.Rectangle) {
                mEraserCanvas.drawRect(mPos[0] - padding, mPos[1] - padding, mPos[0] + mViewHole.getWidth() + padding, mPos[1] + mViewHole.getHeight() + padding, mEraser);
            } else {
                mEraserCanvas.drawCircle(mPos[0] + mViewHole.getWidth() / 2, mPos[1] + mViewHole.getHeight() / 2, mRadius, mEraser);
            }
        }
        canvas.drawBitmap(mEraserBitmap, 0, 0, null);

    }
    /**
     *
     * Convenient method to obtain screen width in pixel
     *
     * @param activity
     * @return screen width in pixel
     */
    public int getScreenWidth(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    /**
     *
     * Convenient method to obtain screen height in pixel
     *
     * @param activity
     * @return screen width in pixel
     */
    public int getScreenHeight(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }
}
