package tourguide.tourguide

import android.animation.AnimatorSet
import android.app.Activity
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import tourguide.tourguide.util.letWith
import tourguide.tourguide.util.locationOnScreen

open class FrameLayoutWithHole @JvmOverloads constructor(private val mActivity: Activity, private var mViewHole: View // This is the targeted view to be highlighted, where the hole should be placed
                                                         , private val mMotionType: TourGuide.MotionType? = TourGuide.MotionType.ALLOW_ALL, private val mOverlay: Overlay? = Overlay()) : FrameLayout(mActivity) {
    private var mTextPaint: TextPaint? = null
    private var mEraser: Paint? = null

    internal var mEraserBitmap: Bitmap? = null
    private var _eraserCanvas: Canvas? = null
    private var mPaint: Paint? = null
    private var transparentPaint: Paint? = null
    private var mRadius: Int = 0
    private val mPosition: Point
        get() = mViewHole.locationOnScreen
    private val mDensity: Float
    private var mRectF: RectF? = null

    private val mAnimatorSetArrayList by lazy { mutableListOf<AnimatorSet>() }

    private var mCleanUpLock = false

    init {
        init(null, 0)
        enforceMotionType()

        mDensity = mActivity.resources.displayMetrics.density
        val padding = (20 * mDensity).toInt()

        if (mViewHole.height > mViewHole.width) {
            mRadius = mViewHole.height / 2 + padding
        } else {
            mRadius = mViewHole.width / 2 + padding
        }

        // Init a RectF to be used in OnDraw for a ROUNDED_RECTANGLE Style Overlay
        mOverlay?.also { _overlay ->
            if (_overlay.mStyle === Overlay.Style.ROUNDED_RECTANGLE) {
                val recfFPaddingPx = (_overlay.mPaddingDp * mDensity).toInt()
                mRectF = RectF((mPosition.x - recfFPaddingPx + _overlay.mHoleOffsetLeft).toFloat(),
                        (mPosition.y - recfFPaddingPx + _overlay.mHoleOffsetTop).toFloat(),
                        (mPosition.x + mViewHole.width + recfFPaddingPx + _overlay.mHoleOffsetLeft).toFloat(),
                        (mPosition.y + mViewHole.height + recfFPaddingPx + _overlay.mHoleOffsetTop).toFloat())
            }
        }
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        // val a = getContext()
        //     .obtainStyledAttributes(attrs, FrameLayoutWithHole, defStyle, 0)
        //     .apply { a.recycle() }
        setWillNotDraw(false)
        // Set up a default TextPaint object
        mTextPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        val size = Point()
        size.x = mActivity.resources.displayMetrics.widthPixels
        size.y = mActivity.resources.displayMetrics.heightPixels

        mEraserBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888)
        _eraserCanvas = Canvas(mEraserBitmap!!)

        mPaint = Paint().apply { color = -0x34000000 }
        transparentPaint = Paint().apply {
            color = ContextCompat.getColor(context, android.R.color.transparent)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }

        mEraser = Paint().apply {
            color = -0x1
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            flags = Paint.ANTI_ALIAS_FLAG
        }
    }

    fun setViewHole(viewHole: View) {
        this.mViewHole = viewHole
        enforceMotionType()
    }

    fun addAnimatorSet(animatorSet: AnimatorSet) {
        mAnimatorSetArrayList.add(animatorSet)
    }

    fun cleanUp() {
        parent?.also {
            if (mOverlay?.mExitAnimation != null) {
                performOverlayExitAnimation()
            } else {
                (parent as ViewGroup).removeView(this)
            }
        }
    }

    private fun enforceMotionType() {
        if (mMotionType == TourGuide.MotionType.CLICK_ONLY) {
            mViewHole.setOnTouchListener { view, motionEvent ->
                mViewHole.parent.requestDisallowInterceptTouchEvent(true)
                false
            }
        } else if (mMotionType == TourGuide.MotionType.SWIPE_ONLY) {
            mViewHole.isClickable = false
        }
    }

    private fun performOverlayExitAnimation() {
        if (!mCleanUpLock) {
            val pointerToFrameLayout = this
            mCleanUpLock = true
            mOverlay?.mExitAnimation?.also {
                it.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}

                    override fun onAnimationRepeat(animation: Animation) {}

                    override fun onAnimationEnd(animation: Animation) {
                        (pointerToFrameLayout.parent as ViewGroup).removeView(pointerToFrameLayout)
                    }
                })
                startAnimation(it)
            }
        }
    }

    /* comment this whole method to cause a memory leak */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        /* cleanup reference to prevent memory leak */
        _eraserCanvas?.setBitmap(null)
        mEraserBitmap = null

        if (!mAnimatorSetArrayList.isEmpty()) {
            for (i in mAnimatorSetArrayList.indices) {
                mAnimatorSetArrayList[i].end()
                mAnimatorSetArrayList[i].removeAllListeners()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //first check if the location button should handle the touch event
        //        ev.dumpEvent();
        //        int action = MotionEventCompat.getActionMasked(ev);
        if (isWithinButton(ev) && mOverlay != null && mOverlay.mDisableClickThroughHole) {
            // block it
            return true
        } else if (isWithinButton(ev)) {
            // let it pass through
            return false
        }
        // do nothing, just propagating up to super
        return super.dispatchTouchEvent(ev)
    }

    private fun isWithinButton(ev: MotionEvent): Boolean {
        val pos = IntArray(2)
        mViewHole.getLocationOnScreen(pos)
        return ev.rawY >= pos[1] &&
                ev.rawY <= pos[1] + mViewHole.height &&
                ev.rawX >= pos[0] &&
                ev.rawX <= pos[0] + mViewHole.width
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mEraserBitmap!!.eraseColor(Color.TRANSPARENT)

        mOverlay.letWith(_eraserCanvas) { _overlay, _eraserCanvas ->
            _eraserCanvas.drawColor(_overlay.backgroundColor)
            val padding = (_overlay.mPaddingDp * mDensity).toInt()

            if (_overlay.mStyle === Overlay.Style.RECTANGLE) {
                _eraserCanvas.drawRect(
                        (mPosition.x - padding + _overlay.mHoleOffsetLeft).toFloat(),
                        (mPosition.y - padding + _overlay.mHoleOffsetTop).toFloat(),
                        (mPosition.x + mViewHole.width + padding + _overlay.mHoleOffsetLeft).toFloat(),
                        (mPosition.y + mViewHole.height + padding + _overlay.mHoleOffsetTop).toFloat(), mEraser!!)
            } else if (_overlay.mStyle === Overlay.Style.NO_HOLE) {
                _eraserCanvas.drawCircle(
                        (mPosition.x + mViewHole.width / 2 + _overlay.mHoleOffsetLeft).toFloat(),
                        (mPosition.y + mViewHole.height / 2 + _overlay.mHoleOffsetTop).toFloat(),
                        0f, mEraser!!)
            } else if (_overlay.mStyle === Overlay.Style.ROUNDED_RECTANGLE) {
                val roundedCornerRadiusPx: Int
                if (_overlay.mRoundedCornerRadiusDp != 0) {
                    roundedCornerRadiusPx = (_overlay.mRoundedCornerRadiusDp * mDensity).toInt()
                } else {
                    roundedCornerRadiusPx = (10 * mDensity).toInt()
                }
                _eraserCanvas.drawRoundRect(mRectF!!, roundedCornerRadiusPx.toFloat(), roundedCornerRadiusPx.toFloat(), mEraser!!)
            } else {
                val holeRadius = if (_overlay.mHoleRadius != Overlay.NOT_SET) _overlay.mHoleRadius else mRadius
                _eraserCanvas.drawCircle(
                        (mPosition.x + mViewHole.width / 2 + _overlay.mHoleOffsetLeft).toFloat(),
                        (mPosition.y + mViewHole.height / 2 + _overlay.mHoleOffsetTop).toFloat(),
                        holeRadius.toFloat(), mEraser!!)
            }
        }
        canvas.drawBitmap(mEraserBitmap!!, 0f, 0f, null)

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mOverlay?.letWith(mOverlay.mEnterAnimation) { _, _enterAnimation ->
            startAnimation(_enterAnimation)
        }
    }
}
