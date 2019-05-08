package tourguide.tourguide

import android.animation.AnimatorSet
import android.annotation.SuppressLint
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

@SuppressLint("ViewConstructor")
open class FrameLayoutWithHole @JvmOverloads constructor(
        private val mActivity: Activity,
        // This is the targeted views to be highlighted, where the hole should be placed
        views: List<ViewHole>,
        private val mOverlay: Overlay? = Overlay()
) : FrameLayout(mActivity) {
    private var mTextPaint: TextPaint? = null
    private var mEraser: Paint? = null

    internal var mEraserBitmap: Bitmap? = null
    private var _eraserCanvas: Canvas? = null
    private var mPaint: Paint? = null
    private var transparentPaint: Paint? = null

    private val mAnimatorSetArrayList by lazy { mutableListOf<AnimatorSet>() }
    private val viewHoles = mutableListOf<InternalViewHole>()

    private var mCleanUpLock = false

    init {
        init(null, 0)
        viewHoles.addAll(createViewHoles(views))
        enforceMotionType()
    }

    private fun createViewHoles(views: List<ViewHole>): List<InternalViewHole> {
        return views.map {
            val (view, config) = it
            when (val shape = config.shape) {
                is Config.Shape.Circle -> {
                    val padding = resources.getDimensionPixelSize(R.dimen.default_padding_circle_shape)
                    val radius = if (view.height > view.width) {
                        view.height / 2 + padding
                    } else {
                        view.width / 2 + padding
                    }
                    InternalViewHole.Circle(view, config, radius)
                }
                is Config.Shape.RoundedRectangle -> {
                    val rectFPaddingPx = shape.padding
                    val position = view.locationOnScreen
                    val rectF = RectF((position.x - rectFPaddingPx + config.offsetLeft).toFloat(),
                            (position.y - rectFPaddingPx + config.offsetTop).toFloat(),
                            (position.x + view.width + rectFPaddingPx + config.offsetLeft).toFloat(),
                            (position.y + view.height + rectFPaddingPx + config.offsetTop).toFloat())
                    InternalViewHole.RoundedRectangle(view, config, rectF)
                }
                else -> InternalViewHole.Common(view, config)
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

    fun setViewHoles(views: List<ViewHole>) {
        viewHoles.clear()
        viewHoles.addAll(createViewHoles(views))
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
        viewHoles.forEach {
            val view = it.view
            val motionType = it.config.motionType
            if (motionType == Config.MotionType.CLICK_ONLY) {
                view.setOnTouchListener { v, motionEvent ->
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    false
                }
            } else if (motionType == Config.MotionType.SWIPE_ONLY) {
                view.isClickable = false
            }
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

        if (mAnimatorSetArrayList.isNotEmpty()) {
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
        viewHoles.forEach {
            if (isWithinButton(ev, it.view)) {
                return if (it.config.canClickThroughHole) {
                    if (ev.action != MotionEvent.ACTION_MOVE) {
                        it.config.onHoleClickListener?.invoke(it.view)
                    }
                    false
                } else {
                    if (ev.action == MotionEvent.ACTION_UP) {
                        it.config.onHoleClickListener?.invoke(it.view)
                    }
                    true
                }
            }
        }
        // do nothing, just propagating up to super
        return super.dispatchTouchEvent(ev)
    }

    private fun isWithinButton(ev: MotionEvent, view: View): Boolean {
        val point = view.locationOnScreen
        return ev.rawY >= point.y &&
                ev.rawY <= point.y + view.height &&
                ev.rawX >= point.x &&
                ev.rawX <= point.x + view.width
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mEraserBitmap!!.eraseColor(Color.TRANSPARENT)

        mOverlay.letWith(_eraserCanvas) { _overlay, _eraserCanvas ->
            _eraserCanvas.drawColor(_overlay.backgroundColor)
            viewHoles.forEach {
                val config = it.config
                val position = it.position
                val view = it.view
                val shape = config.shape
                when (it) {
                    is InternalViewHole.Common -> {
                        when (shape) {
                            Config.Shape.NoHole -> {
                                _eraserCanvas.drawCircle(
                                        (position.x + view.width / 2 + config.offsetLeft).toFloat(),
                                        (position.y + view.height / 2 + config.offsetTop).toFloat(),
                                        0f, mEraser!!)
                            }
                            is Config.Shape.Rectangle -> {
                                val padding = shape.padding
                                _eraserCanvas.drawRect(
                                        (position.x - padding + config.offsetLeft).toFloat(),
                                        (position.y - padding + config.offsetTop).toFloat(),
                                        (position.x + view.width + padding + config.offsetLeft).toFloat(),
                                        (position.y + view.height + padding + config.offsetTop).toFloat(), mEraser!!)
                            }
                            is Config.Shape.DrawPath -> {
                                _eraserCanvas.drawPath(shape.path, mEraser!!)
                            }
                            else -> throw IllegalStateException("Shape $shape does not supported for ViewHole = ${it::class.java.simpleName}")
                        }
                    }
                    is InternalViewHole.Circle -> {
                        if (shape is Config.Shape.Circle) {
                            val holeRadius = if (shape.holeRadius != Config.Shape.Circle.NOT_SET) shape.holeRadius else it.radius
                            _eraserCanvas.drawCircle(
                                    (position.x + view.width / 2 + config.offsetLeft).toFloat(),
                                    (position.y + view.height / 2 + config.offsetTop).toFloat(),
                                    holeRadius.toFloat(), mEraser!!)
                        } else {
                            throw IllegalStateException("Shape $shape does not supported for ViewHole = ${it::class.java.simpleName}")
                        }
                    }
                    is InternalViewHole.RoundedRectangle -> {
                        if (shape is Config.Shape.RoundedRectangle) {
                            val roundedCornerRadiusPx = if (shape.cornerRadius != Config.Shape.RoundedRectangle.NOT_SET) {
                                shape.cornerRadius
                            } else {
                                resources.getDimensionPixelSize(R.dimen.default_corner_radius)
                            }
                            _eraserCanvas.drawRoundRect(it.rectF, roundedCornerRadiusPx.toFloat(), roundedCornerRadiusPx.toFloat(), mEraser!!)
                        } else {
                            throw IllegalStateException("Shape $shape does not supported for ViewHole = ${it::class.java.simpleName}")
                        }
                    }
                }
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

    private sealed class InternalViewHole(val view: View, val config: Config) {

        val position: Point
            get() = view.locationOnScreen

        class Common(view: View, config: Config) : InternalViewHole(view, config)
        class Circle(view: View, config: Config, val radius: Int) : InternalViewHole(view, config)
        class RoundedRectangle(view: View, config: Config, val rectF: RectF) : InternalViewHole(view, config)
    }
}
