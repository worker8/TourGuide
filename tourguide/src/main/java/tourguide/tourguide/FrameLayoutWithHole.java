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
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;

    private float mTextHeight;
    private Paint mOverlayPaint;
    private Activity mActivity;
    private AnimateTutorial.MotionType mMotionType;

    public View getViewHole() {
        return mViewHole;
    }

    public void setViewHole(View viewHole) {
        this.mViewHole = viewHole;
        enforceMotionType();
    }

    private void enforceMotionType(){
        Log.d("ddw","enforceMotionType 1");
        if (mViewHole!=null) {Log.d("ddw","enforceMotionType 2");
            if (mMotionType!=null && mMotionType == AnimateTutorial.MotionType.ClickOnly) {
                Log.d("ddw","enforceMotionType 3");
                Log.d("ddw","only Swiping");
                mViewHole.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        mViewHole.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
            } else if (mMotionType!=null && mMotionType == AnimateTutorial.MotionType.SwipeOnly) {
                Log.d("ddw","enforceMotionType 4");
                Log.d("ddw","only Swiping");
                mViewHole.setClickable(false);
            }
        }
    }
    private View mViewHole; // This is the targeted view to be highlighted, where the hole should be placed
    public FrameLayoutWithHole(Activity context) {
        super(context);
        mActivity = context;
        init(null, 0);
    }
    public FrameLayoutWithHole(Activity context, AnimateTutorial.MotionType motionType) {
        super(context);
        mActivity = context;
        init(null, 0);
        mMotionType = motionType;
    }
    public FrameLayoutWithHole(Activity context, View view) {
        super(context);
        mActivity = context;
        init(null, 0);
        mViewHole = view;
        enforceMotionType();
        mMotionType = AnimateTutorial.MotionType.AllowAll;
    }

    public FrameLayoutWithHole(Activity context, View view, AnimateTutorial.MotionType motionType) {
        super(context);
        mActivity = context;
        init(null, 0);
        mViewHole = view;
        enforceMotionType();
        mMotionType = motionType;
    }

    public FrameLayoutWithHole(Activity context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = context;
        init(attrs, 0);
    }

    public FrameLayoutWithHole(Activity context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mActivity = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
//        final TypedArray a = getContext().obtainStyledAttributes(
//                attrs, FrameLayoutWithHole, defStyle, 0);
//
//
//        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
//        invalidateTextPaintAndMeasurements();
//        setWillNotDraw(false);

        bitmapx = Bitmap.createBitmap(getScreenWidth(mActivity), getScreenHeight(mActivity), Bitmap.Config.ARGB_8888);
        temp = new Canvas(bitmapx);
        paint = new Paint();
        paint.setColor(0xcc000000);
        transparentPaint = new Paint();
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        if (mViewHole!=null) {
            Log.d("ddw","mViewHole disable intercept of parentz ");
            mViewHole.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mViewHole.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
        }
    }
    Bitmap bitmapx;
    private Canvas temp;
    private Paint paint;
    private Paint p = new Paint();
    private Paint transparentPaint;
    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
//        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
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
    float mMemoryX;
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

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;


        // Draw the text.
//        canvas.drawText(mExampleString,
//                paddingLeft + (contentWidth - mTextWidth) / 2,
//                paddingTop + (contentHeight + mTextHeight) / 2,
//                mTextPaint);

        temp.drawRect(0, 0, temp.getWidth(), temp.getHeight(), paint);
//        temp.drawCircle(300,300, 150, transparentPaint);
//        canvas.drawBitmap(bitmapx, 0, 0, p);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
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
