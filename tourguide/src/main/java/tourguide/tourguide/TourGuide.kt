package tourguide.tourguide

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.support.v4.view.ViewCompat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.tourguide_tooltip.view.*
import net.i2p.android.ext.floatingactionbutton.FloatingActionButton
import tourguide.tourguide.util.locationOnScreen

open class TourGuide(private val activity: Activity) {
    var technique: Technique = Technique.CLICK
    protected lateinit var highlightedView: View
    private var motionType: MotionType = MotionType.CLICK_ONLY
    /**
     *
     * @return FrameLayoutWithHole that is used as overlay
     */
    var frameLayoutWithHole: FrameLayoutWithHole? = null
        protected set
    /**
     *
     * @return the ToolTip container View
     */
    var toolTipView: View? = null
        private set
    var toolTip: ToolTip? = null
        private set
    var mPointer: Pointer? = null
    var overlay: Overlay? = null

    private val screenWidth: Int
        get() = activity.resources?.displayMetrics?.widthPixels ?: 0

    /**
     * This describes the animation techniques
     */
    enum class Technique {
        CLICK, HORIZONTAL_LEFT, HORIZONTAL_RIGHT, VERTICAL_UPWARD, VERTICAL_DOWNWARD
    }

    /**
     * This describes the allowable motion, for example if you want the users to learn about clicking, but want to stop them from swiping, then use CLICK_ONLY
     */
    enum class MotionType {
        ALLOW_ALL, CLICK_ONLY, SWIPE_ONLY
    }

    /**
     * Setter for the animation to be used
     * @param technique Animation to be used
     * @return return TourGuide instance for chaining purpose
     */
    open fun with(technique: Technique): TourGuide {
        this.technique = technique
        return this
    }

    /**
     * Sets which motion type is motionType
     * @param motionType
     * @return return TourGuide instance for chaining purpose
     */
    open fun motionType(_motionType: MotionType): TourGuide {
        motionType = _motionType
        return this
    }

    /**
     * Sets the targeted view for TourGuide to play on
     * @param targetView the view in which the tutorial button will be placed on top of
     * @return return TourGuide instance for chaining purpose
     */
    open infix fun playOn(targetView: View): TourGuide {
        highlightedView = targetView
        setupView()
        return this
    }

    /**
     * Set the toolTipView
     * @param toolTip this toolTipView object should contain the attributes of the ToolTip, such as, the title text, and the description text, background color, etc
     * @return return TourGuide instance for chaining purpose
     */
    open fun setToolTip(toolTip: ToolTip): TourGuide {
        this.toolTip = toolTip
        return this
    }

    /**
     * Set the Pointer
     * @param pointer this pointer object should contain the attributes of the Pointer, such as the pointer color, pointer gravity, etc, refer to @Link{pointer}
     * @return return TourGuide instance for chaining purpose
     */
    open fun setPointer(pointer: Pointer): TourGuide {
        mPointer = pointer
        return this
    }

    /**
     * Clean up the tutorial that is added to the activity
     */
    fun cleanUp() {
        frameLayoutWithHole?.cleanUp()
        toolTipView.also {
            (activity.window.decorView as ViewGroup).removeView(it)
        }
    }

    //TODO: move into Pointer
    private fun getXBasedOnGravity(width: Int): Int {
        val x = highlightedView.locationOnScreen.x
        mPointer?.let { _pointer ->
            if (_pointer.gravity and Gravity.RIGHT == Gravity.RIGHT) {
                return x + highlightedView.width - width
            } else if (_pointer.gravity and Gravity.LEFT == Gravity.LEFT) {
                return x
            }
        }
        return x + highlightedView.width / 2 - width / 2
    }

    //TODO: move into Pointer
    private fun getYBasedOnGravity(height: Int): Int {
        val y = highlightedView.locationOnScreen.y
        mPointer?.let { _pointer ->
            if (_pointer.gravity and Gravity.BOTTOM == Gravity.BOTTOM) {
                return y + highlightedView.height - height
            } else if (_pointer.gravity and Gravity.TOP == Gravity.TOP) {
                return y
            }
        }
        return y + highlightedView.height / 2 - height / 2
    }

    protected fun setupView() {
        // TourGuide can only be setup after all the views is ready and obtain it's position/measurement
        // so when this is the 1st time TourGuide is being added,
        // else block will be executed, and ViewTreeObserver will make TourGuide setup process to be delayed until everything is ready
        // when this is run the 2nd or more times, if block will be executed
        if (ViewCompat.isAttachedToWindow(highlightedView)) {
            startView()
        } else {
            val viewTreeObserver = highlightedView.viewTreeObserver
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        highlightedView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    } else {
                        highlightedView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                    startView()
                }
            })
        }
    }

    private fun startView() {
        /* Initialize a frame layout with a hole */
        frameLayoutWithHole = FrameLayoutWithHole(activity, highlightedView, motionType, overlay)
        /* handle click disable */
        frameLayoutWithHole?.let {
            handleDisableClicking(it)
        }

        /* setup floating action button */
        if (mPointer != null) {
            frameLayoutWithHole?.let {
                val fab = setupAndAddFABToFrameLayout(it)
                performAnimationOn(fab)
            }
        }
        setupFrameLayout()
        /* setup tourguide_tooltip view */
        setupToolTip()
    }

    private fun handleDisableClicking(frameLayoutWithHole: FrameLayoutWithHole) {
        overlay?.also { _overlay ->
            if (_overlay.mOnClickListener != null) {
                // 1. if user provides an overlay listener, use that as 1st priority
                frameLayoutWithHole.isClickable = true
                frameLayoutWithHole.setOnClickListener(_overlay.mOnClickListener)
            } else if (_overlay.mDisableClick) {
                // 2. if overlay listener is not provided, check if it's disabled
                Log.w("tourguide", "Overlay's default OnClickListener is null, it will proceed to next tourguide when it is clicked")
                frameLayoutWithHole.setViewHole(highlightedView)
                frameLayoutWithHole.isSoundEffectsEnabled = false
                frameLayoutWithHole.setOnClickListener { }
            }
        }
    }

    private fun setupToolTip() {
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)

        toolTip?.also { _toolTip ->

            /* inflate and get views */
            val parent = activity.window.decorView as ViewGroup
            val layoutInflater = activity.layoutInflater

            if (_toolTip.getCustomView() == null) {
                toolTipView = layoutInflater.inflate(R.layout.tourguide_tooltip, null)
                toolTipView?.apply {

                    /* set tourguide_tooltip attributes */
                    toolTipContainer.setBackgroundColor(_toolTip.mBackgroundColor)
                    toolTipTitleTextView.setTextColor(_toolTip.mTextColor)
                    toolTipDescTextView.setTextColor(_toolTip.mTextColor)

                    if (_toolTip.title.isEmpty()) {
                        toolTipTitleTextView.visibility = View.GONE
                    } else {
                        toolTipTitleTextView.visibility = View.VISIBLE
                        toolTipTitleTextView.text = _toolTip.title
                    }

                    if (_toolTip.description.isEmpty()) {
                        toolTipDescTextView.visibility = View.GONE
                    } else {
                        toolTipDescTextView.visibility = View.VISIBLE
                        toolTipDescTextView.text = _toolTip.description
                    }

                    if (_toolTip.mWidth != -1) {
                        layoutParams.width = _toolTip.mWidth
                    }
                }
            } else {
                toolTipView = _toolTip.getCustomView()
            }
            toolTipView?.also { _toolTipView ->
                _toolTipView.startAnimation(_toolTip.mEnterAnimation)

                /* add setShadow if it's turned on */
                if (_toolTip.mShadow) {
                    _toolTipView.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.tourguide_drop_shadow))
                }

                /* position and size calculation */
                val pos = IntArray(2)
                highlightedView.getLocationOnScreen(pos)
                val targetViewX = pos[0]
                val targetViewY = pos[1]

                // get measured size of tourguide_tooltip
                _toolTipView.measure(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                var toolTipMeasuredWidth = if (_toolTip.mWidth != -1) _toolTip.mWidth else _toolTipView.measuredWidth
                val toolTipMeasuredHeight = _toolTipView.measuredHeight

                val resultPoint = Point() // this holds the final position of tourguide_tooltip
                val density = activity.resources.displayMetrics.density
                val adjustment = 10 * density //adjustment is that little overlapping area of tourguide_tooltip and targeted button

                // calculate x position, based on gravity, tooltipMeasuredWidth, parent max width, x position of target view, adjustment
                if (toolTipMeasuredWidth > parent.width) {
                    resultPoint.x = getXForTooTip(_toolTip.mGravity, parent.width, targetViewX, adjustment)
                } else {
                    resultPoint.x = getXForTooTip(_toolTip.mGravity, toolTipMeasuredWidth, targetViewX, adjustment)
                }

                resultPoint.y = getYForTooTip(_toolTip.mGravity, toolTipMeasuredHeight, targetViewY, adjustment)

                parent.addView(_toolTipView, layoutParams)

                // 1. width < screen check
                if (toolTipMeasuredWidth > parent.width) {
                    _toolTipView.layoutParams.width = parent.width
                    toolTipMeasuredWidth = parent.width
                }
                // 2. x left boundary check
                if (resultPoint.x < 0) {
                    _toolTipView.layoutParams.width = toolTipMeasuredWidth + resultPoint.x //since point.x is negative, use plus
                    resultPoint.x = 0
                }
                // 3. x right boundary check
                val tempRightX = resultPoint.x + toolTipMeasuredWidth
                if (tempRightX > parent.width) {
                    _toolTipView.layoutParams.width = parent.width - resultPoint.x //since point.x is negative, use plus
                }

                // pass toolTipView onClickListener into toolTipViewGroup
                if (_toolTip.mOnClickListener != null) {
                    _toolTipView.setOnClickListener(_toolTip.mOnClickListener)
                }

                // TODO: no boundary check for height yet, this is a unlikely case though
                // height boundary can be fixed by user changing the gravity to the other size, since there are plenty of space vertically compared to horizontally

                // this needs an viewTreeObserver, that's because TextView measurement of it's vertical height is not accurate (didn't take into account of multiple lines yet) before it's rendered
                // re-calculate height again once it's rendered
                _toolTipView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        // make sure this only run once
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            _toolTipView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                        } else {
                            _toolTipView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }

                        val fixedY: Int
                        val toolTipHeightAfterLayouted = _toolTipView.height
                        fixedY = getYForTooTip(_toolTip.mGravity, toolTipHeightAfterLayouted, targetViewY, adjustment)
                        layoutParams.setMargins(_toolTipView.x.toInt(), fixedY, 0, 0)
                    }
                })

                // set the position using setMargins on the left and top
                layoutParams.setMargins(resultPoint.x, resultPoint.y, 0, 0)
            }
        }

    }

    private fun getXForTooTip(gravity: Int, toolTipMeasuredWidth: Int, targetViewX: Int, adjustment: Float) =
            when {
                gravity and Gravity.LEFT == Gravity.LEFT -> targetViewX - toolTipMeasuredWidth + adjustment.toInt()
                gravity and Gravity.RIGHT == Gravity.RIGHT -> targetViewX + highlightedView.width - adjustment.toInt()
                else -> targetViewX + highlightedView.width / 2 - toolTipMeasuredWidth / 2
            }

    private fun getYForTooTip(gravity: Int, toolTipMeasuredHeight: Int, targetViewY: Int, adjustment: Float) =
            when {
                gravity and Gravity.TOP == Gravity.TOP -> if (gravity and Gravity.LEFT == Gravity.LEFT || gravity and Gravity.RIGHT == Gravity.RIGHT) {
                    targetViewY - toolTipMeasuredHeight + adjustment.toInt()
                } else {
                    targetViewY - toolTipMeasuredHeight - adjustment.toInt()
                }
                else -> // this is center
                    if (gravity and Gravity.LEFT == Gravity.LEFT || gravity and Gravity.RIGHT == Gravity.RIGHT) {
                        targetViewY + highlightedView.height - adjustment.toInt()
                    } else {
                        targetViewY + highlightedView.height + adjustment.toInt()
                    }
            }

    private fun setupAndAddFABToFrameLayout(frameLayoutWithHole: FrameLayoutWithHole): FloatingActionButton {
        // invisFab is invisible, and it's only used for getting the width and height
        val invisFab = FloatingActionButton(activity)
        invisFab.size = FloatingActionButton.SIZE_MINI
        invisFab.visibility = View.INVISIBLE
        (activity.window.decorView as ViewGroup).addView(invisFab)

        // fab is the real fab that is going to be added
        return FloatingActionButton(activity).apply {
            setBackgroundColor(Color.BLUE)
            size = FloatingActionButton.SIZE_MINI
            mPointer?.also { colorNormal = it.color }
            isStrokeVisible = false
            isClickable = false

            // When invisFab is layouted, it's width and height can be used to calculate the correct position of fab
            invisFab.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // make sure this only run once
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {

                        invisFab.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    } else {
                        invisFab.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                    val params = FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    frameLayoutWithHole.addView(this@apply, params)

                    // measure size of image to be placed
                    params.setMargins(getXBasedOnGravity(invisFab.width), getYBasedOnGravity(invisFab.height), 0, 0)
                }
            })
        }
    }

    private fun setupFrameLayout() {
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        val contentArea = activity.window.decorView.findViewById(android.R.id.content) as ViewGroup
        val pos = IntArray(2)
        contentArea.getLocationOnScreen(pos)
        // frameLayoutWithHole's coordinates are calculated taking full screen height into account
        // but we're adding it to the content area only, so we need to offset it to the same Y value of contentArea

        layoutParams.setMargins(0, -pos[1], 0, 0)
        contentArea.addView(frameLayoutWithHole, layoutParams)
    }

    private fun performAnimationOn(view: View) {

        if (technique == Technique.HORIZONTAL_LEFT) {

            val animatorSet = AnimatorSet()
            val animatorSet2 = AnimatorSet()
            val lis1 = object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    view.scaleX = 1f
                    view.scaleY = 1f
                    view.translationX = 0f
                    animatorSet2.start()
                }
            }
            val lis2 = object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    view.scaleX = 1f
                    view.scaleY = 1f
                    view.translationX = 0f
                    animatorSet.start()
                }
            }

            val fadeInDuration: Long = 800
            val scaleDownDuration: Long = 800
            val goLeftXDuration: Long = 2000
            val translationX = (screenWidth / 2).toFloat()

            val fadeInAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            fadeInAnim.duration = fadeInDuration
            val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f)
            scaleDownX.duration = scaleDownDuration
            val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f)
            scaleDownY.duration = scaleDownDuration
            val goLeftX = ObjectAnimator.ofFloat(view, "translationX", -translationX)
            goLeftX.duration = goLeftXDuration
            val fadeOutAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
            fadeOutAnim.duration = goLeftXDuration

            val fadeInAnim2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            fadeInAnim2.duration = fadeInDuration
            val scaleDownX2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f)
            scaleDownX2.duration = scaleDownDuration
            val scaleDownY2 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f)
            scaleDownY2.duration = scaleDownDuration
            val goLeftX2 = ObjectAnimator.ofFloat(view, "translationX", -translationX)
            goLeftX2.duration = goLeftXDuration
            val fadeOutAnim2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
            fadeOutAnim2.duration = goLeftXDuration

            animatorSet.play(fadeInAnim)
            animatorSet.play(scaleDownX).with(scaleDownY).after(fadeInAnim)
            animatorSet.play(goLeftX).with(fadeOutAnim).after(scaleDownY)

            animatorSet2.play(fadeInAnim2)
            animatorSet2.play(scaleDownX2).with(scaleDownY2).after(fadeInAnim2)
            animatorSet2.play(goLeftX2).with(fadeOutAnim2).after(scaleDownY2)

            animatorSet.addListener(lis1)
            animatorSet2.addListener(lis2)
            animatorSet.start()

            /* these animatorSets are kept track in FrameLayout, so that they can be cleaned up when FrameLayout is detached from window */
            frameLayoutWithHole?.addAnimatorSet(animatorSet)
            frameLayoutWithHole?.addAnimatorSet(animatorSet2)
        } else if (technique == Technique.HORIZONTAL_RIGHT) { //TODO: new feature

        } else if (technique == Technique.VERTICAL_UPWARD) {//TODO: new feature

        } else if (technique == Technique.VERTICAL_DOWNWARD) {//TODO: new feature

        } else { // do click for default case
            val animatorSet = AnimatorSet()
            val animatorSet2 = AnimatorSet()
            val lis1 = object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    view.scaleX = 1f
                    view.scaleY = 1f
                    view.translationX = 0f
                    animatorSet2.start()
                }
            }
            val lis2 = object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    view.scaleX = 1f
                    view.scaleY = 1f
                    view.translationX = 0f
                    animatorSet.start()
                }
            }

            val fadeInDuration: Long = 800
            val scaleDownDuration: Long = 800
            val fadeOutDuration: Long = 800
            val delay: Long = 1000

            val delayAnim = ObjectAnimator.ofFloat(view, "translationX", 0f)
            delayAnim.setDuration(delay)
            val fadeInAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            fadeInAnim.duration = fadeInDuration
            val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f)
            scaleDownX.duration = scaleDownDuration
            val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f)
            scaleDownY.duration = scaleDownDuration
            val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 0.85f, 1f)
            scaleUpX.duration = scaleDownDuration
            val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 0.85f, 1f)
            scaleUpY.duration = scaleDownDuration
            val fadeOutAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
            fadeOutAnim.duration = fadeOutDuration

            val delayAnim2 = ObjectAnimator.ofFloat(view, "translationX", 0f)
            delayAnim2.setDuration(delay)
            val fadeInAnim2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            fadeInAnim2.duration = fadeInDuration
            val scaleDownX2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f)
            scaleDownX2.duration = scaleDownDuration
            val scaleDownY2 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f)
            scaleDownY2.duration = scaleDownDuration
            val scaleUpX2 = ObjectAnimator.ofFloat(view, "scaleX", 0.85f, 1f)
            scaleUpX2.duration = scaleDownDuration
            val scaleUpY2 = ObjectAnimator.ofFloat(view, "scaleY", 0.85f, 1f)
            scaleUpY2.duration = scaleDownDuration
            val fadeOutAnim2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
            fadeOutAnim2.duration = fadeOutDuration
            view.alpha = 0f
            animatorSet.startDelay = toolTip?.run { mEnterAnimation.duration } ?: 0
            animatorSet.play(fadeInAnim)
            animatorSet.play(scaleDownX).with(scaleDownY).after(fadeInAnim)
            animatorSet.play(scaleUpX).with(scaleUpY).with(fadeOutAnim).after(scaleDownY)
            animatorSet.play(delayAnim).after(scaleUpY)

            animatorSet2.play(fadeInAnim2)
            animatorSet2.play(scaleDownX2).with(scaleDownY2).after(fadeInAnim2)
            animatorSet2.play(scaleUpX2).with(scaleUpY2).with(fadeOutAnim2).after(scaleDownY2)
            animatorSet2.play(delayAnim2).after(scaleUpY2)

            animatorSet.addListener(lis1)
            animatorSet2.addListener(lis2)
            animatorSet.start()

            /* these animatorSets are kept track in FrameLayout, so that they can be cleaned up when FrameLayout is detached from window */
            frameLayoutWithHole?.addAnimatorSet(animatorSet)
            frameLayoutWithHole?.addAnimatorSet(animatorSet2)
        }
    }

    fun pointer(block: Pointer.() -> Unit) {
        mPointer = Pointer().apply { block() }
    }

    fun toolTip(block: ToolTip.() -> Unit) {
        toolTip = ToolTip().apply { block() }
    }

    fun overlay(block: Overlay.() -> Unit) {
        overlay = Overlay().apply { block() }
    }

    companion object {
        @JvmStatic
        open fun init(activity: Activity) = TourGuide(activity)

        fun create(activity: Activity, block: TourGuide.() -> Unit): TourGuide {
            return TourGuide(activity).apply { block() }
        }
    }
}
