package tourguide.tourguide

import android.animation.AnimatorSet
import android.app.Activity
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import java.util.*

open class FrameLayoutWithHole @JvmOverloads constructor(private val mActivity: Activity, private var mViewHole: View? // This is the targeted view to be highlighted, where the hole should be placed
                                                         , private val mMotionType: TourGuide.MotionType? = TourGuide.MotionType.ALLOW_ALL, private val mOverlay: Overlay? = Overlay()) : FrameLayout(mActivity) {
    private var mTextPaint: TextPaint? = null
    private var mEraser: Paint? = null

    internal var mEraserBitmap: Bitmap? = null
    private var mEraserCanvas: Canvas? = null
    private var mPaint: Paint? = null
    private var transparentPaint: Paint? = null
    private var mRadius: Int = 0
    private val mPos: IntArray
    private val mDensity: Float
    private var mRectF: RectF? = null

    private var mAnimatorSetArrayList: ArrayList<AnimatorSet>? = null

    private var mCleanUpLock = false

    fun setViewHole(viewHole: View) {
        this.mViewHole = viewHole
        enforceMotionType()
    }

    fun addAnimatorSet(animatorSet: AnimatorSet) {
        if (mAnimatorSetArrayList == null) {
            mAnimatorSetArrayList = ArrayList()
        }
        mAnimatorSetArrayList!!.add(animatorSet)
    }

    private fun enforceMotionType() {
        if (mViewHole != null) {
            if (mMotionType != null && mMotionType === TourGuide.MotionType.CLICK_ONLY) {
                mViewHole!!.setOnTouchListener { view, motionEvent ->
                    mViewHole!!.parent.requestDisallowInterceptTouchEvent(true)
                    false
                }
            } else if (mMotionType != null && mMotionType === TourGuide.MotionType.SWIPE_ONLY) {
                mViewHole!!.isClickable = false
            }
        }
    }

    init {
        init(null, 0)
        enforceMotionType()

        val pos = IntArray(2)
        mViewHole!!.getLocationOnScreen(pos)
        mPos = pos

        mDensity = mActivity.resources.displayMetrics.density
        val padding = (20 * mDensity).toInt()

        if (mViewHole!!.height > mViewHole!!.width) {
            mRadius = mViewHole!!.height / 2 + padding
        } else {
            mRadius = mViewHole!!.width / 2 + padding
        }

        // Init a RectF to be used in OnDraw for a ROUNDED_RECTANGLE Style Overlay
        if (mOverlay != null && mOverlay.mStyle === Overlay.Style.ROUNDED_RECTANGLE) {
            val recfFPaddingPx = (mOverlay.mPaddingDp * mDensity).toInt()
            mRectF = RectF((mPos[0] - recfFPaddingPx + mOverlay.mHoleOffsetLeft).toFloat(),
                    (mPos[1] - recfFPaddingPx + mOverlay.mHoleOffsetTop).toFloat(),
                    (mPos[0] + mViewHole!!.width + recfFPaddingPx + mOverlay.mHoleOffsetLeft).toFloat(),
                    (mPos[1] + mViewHole!!.height + recfFPaddingPx + mOverlay.mHoleOffsetTop).toFloat())
        }
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        //        final TypedArray a = getContext().obtainStyledAttributes(
        //                attrs, FrameLayoutWithHole, defStyle, 0);
        //
        //
        //        a.recycle();
        setWillNotDraw(false)
        // Set up a default TextPaint object
        mTextPaint = TextPaint()
        mTextPaint!!.flags = Paint.ANTI_ALIAS_FLAG
        mTextPaint!!.textAlign = Paint.Align.LEFT

        val size = Point()
        size.x = mActivity.resources.displayMetrics.widthPixels
        size.y = mActivity.resources.displayMetrics.heightPixels

        mEraserBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888)
        mEraserCanvas = Canvas(mEraserBitmap!!)

        mPaint = Paint()
        mPaint!!.color = -0x34000000
        transparentPaint = Paint()
        transparentPaint!!.color = resources.getColor(android.R.color.transparent)
        transparentPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        mEraser = Paint()
        mEraser!!.color = -0x1
        mEraser!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        mEraser!!.flags = Paint.ANTI_ALIAS_FLAG
    }

    fun cleanUp() {
        if (parent != null) {
            if (mOverlay != null && mOverlay.mExitAnimation != null) {
                performOverlayExitAnimation()
            } else {
                (this.parent as ViewGroup).removeView(this)
            }
        }
    }

    private fun performOverlayExitAnimation() {
        if (!mCleanUpLock) {
            val _pointerToFrameLayout = this
            mCleanUpLock = true
            mOverlay!!.mExitAnimation!!.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationRepeat(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    (_pointerToFrameLayout.parent as ViewGroup).removeView(_pointerToFrameLayout)
                }
            })
            this.startAnimation(mOverlay.mExitAnimation)
        }
    }

    /* comment this whole method to cause a memory leak */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        /* cleanup reference to prevent memory leak */
        mEraserCanvas!!.setBitmap(null)
        mEraserBitmap = null

        if (mAnimatorSetArrayList != null && !mAnimatorSetArrayList!!.isEmpty()) {
            for (i in mAnimatorSetArrayList!!.indices) {
                mAnimatorSetArrayList!![i].end()
                mAnimatorSetArrayList!![i].removeAllListeners()
            }
        }
    }

    /**
     * Show an event in the LogCat view, for debugging
     */
    private fun dumpEvent(event: MotionEvent) {
        val names = arrayOf("DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?")
        val sb = StringBuilder()
        val action = event.action
        val actionCode = action and MotionEvent.ACTION_MASK
        sb.append("event ACTION_").append(names[actionCode])
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action shr MotionEvent.ACTION_POINTER_ID_SHIFT)
            sb.append(")")
        }
        sb.append("[")
        for (i in 0 until event.pointerCount) {
            sb.append("#").append(i)
            sb.append("(pid ").append(event.getPointerId(i))
            sb.append(")=").append(event.getX(i).toInt())
            sb.append(",").append(event.getY(i).toInt())
            if (i + 1 < event.pointerCount)
                sb.append(";")
        }
        sb.append("]")
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //first check if the location button should handle the touch event
        //        dumpEvent(ev);
        //        int action = MotionEventCompat.getActionMasked(ev);
        if (mViewHole != null) {
            if (isWithinButton(ev) && mOverlay != null && mOverlay.mDisableClickThroughHole) {
                // block it
                return true
            } else if (isWithinButton(ev)) {
                // let it pass through
                return false
            }
        }
        // do nothing, just propagating up to super
        return super.dispatchTouchEvent(ev)
    }

    private fun isWithinButton(ev: MotionEvent): Boolean {
        val pos = IntArray(2)
        mViewHole!!.getLocationOnScreen(pos)
        return ev.rawY >= pos[1] &&
                ev.rawY <= pos[1] + mViewHole!!.height &&
                ev.rawX >= pos[0] &&
                ev.rawX <= pos[0] + mViewHole!!.width
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mEraserBitmap!!.eraseColor(Color.TRANSPARENT)

        if (mOverlay != null) {
            mEraserCanvas!!.drawColor(mOverlay.backgroundColor)
            val padding = (mOverlay.mPaddingDp * mDensity).toInt()
            Log.i("TOURGUIDE", String.format("**********PADDING: %s**********", padding))

            if (mOverlay.mStyle === Overlay.Style.RECTANGLE) {
                mEraserCanvas!!.drawRect(
                        (mPos[0] - padding + mOverlay.mHoleOffsetLeft).toFloat(),
                        (mPos[1] - padding + mOverlay.mHoleOffsetTop).toFloat(),
                        (mPos[0] + mViewHole!!.width + padding + mOverlay.mHoleOffsetLeft).toFloat(),
                        (mPos[1] + mViewHole!!.height + padding + mOverlay.mHoleOffsetTop).toFloat(), mEraser!!)
            } else if (mOverlay.mStyle === Overlay.Style.NO_HOLE) {
                mEraserCanvas!!.drawCircle(
                        (mPos[0] + mViewHole!!.width / 2 + mOverlay.mHoleOffsetLeft).toFloat(),
                        (mPos[1] + mViewHole!!.height / 2 + mOverlay.mHoleOffsetTop).toFloat(),
                        0f, mEraser!!)
            } else if (mOverlay.mStyle === Overlay.Style.ROUNDED_RECTANGLE) {
                val roundedCornerRadiusPx: Int
                if (mOverlay.mRoundedCornerRadiusDp != 0) {
                    roundedCornerRadiusPx = (mOverlay.mRoundedCornerRadiusDp * mDensity).toInt()
                } else {
                    roundedCornerRadiusPx = (10 * mDensity).toInt()
                }
                mEraserCanvas!!.drawRoundRect(mRectF!!, roundedCornerRadiusPx.toFloat(), roundedCornerRadiusPx.toFloat(), mEraser!!)
            } else {
                val holeRadius = if (mOverlay.mHoleRadius != Overlay.NOT_SET) mOverlay.mHoleRadius else mRadius
                mEraserCanvas!!.drawCircle(
                        (mPos[0] + mViewHole!!.width / 2 + mOverlay.mHoleOffsetLeft).toFloat(),
                        (mPos[1] + mViewHole!!.height / 2 + mOverlay.mHoleOffsetTop).toFloat(),
                        holeRadius.toFloat(), mEraser!!)
            }
        }
        canvas.drawBitmap(mEraserBitmap!!, 0f, 0f, null)

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mOverlay != null && mOverlay.mEnterAnimation != null) {
            this.startAnimation(mOverlay.mEnterAnimation)
        }
    }

    /**
     * Convenient method to obtain screen width in pixel
     *
     * @param activity
     * @return screen width in pixel
     */
    fun getScreenWidth(activity: Activity): Int {
        return activity.resources.displayMetrics.widthPixels
    }

    /**
     * Convenient method to obtain screen height in pixel
     *
     * @param activity
     * @return screen width in pixel
     */
    fun getScreenHeight(activity: Activity): Int {
        return activity.resources.displayMetrics.heightPixels
    }
}
