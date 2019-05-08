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
import net.i2p.android.ext.floatingactionbutton.FloatingActionButton
import tourguide.tourguide.util.locationOnScreen

open class TourGuide(private val activity: Activity) {
    private val highlightedViews = mutableListOf<ViewHole>()
    private val toolTipViews = mutableListOf<View>()
    private var pendingCount = 0
    /**
     *
     * @return FrameLayoutWithHole that is used as overlay
     */
    var frameLayoutWithHole: FrameLayoutWithHole? = null
        protected set
    var overlay: Overlay? = null

    private val screenWidth: Int
        get() = activity.resources?.displayMetrics?.widthPixels ?: 0
    private val screenHeight: Int
        get() = activity.resources?.displayMetrics?.heightPixels ?: 0

    fun playOn(vararg viewHoles: ViewHole): TourGuide {
        viewHoles.forEach { viewHole ->
            highlightedViews.remove(viewHole)
            highlightedViews.add(viewHole)
        }
        return this
    }

    fun playOn(targetView: View, block: Config.() -> Unit = {}): TourGuide {
        val viewHole = ViewHole(targetView, Config().apply(block))
        highlightedViews.remove(viewHole)
        highlightedViews.add(viewHole)
        return this
    }

    fun show(): TourGuide {
        setupView()
        return this
    }

    /**
     * Clean up the tutorial that is added to the activity
     */
    fun cleanUp() {
        reset()
        highlightedViews.clear()
    }

    private fun reset() {
        frameLayoutWithHole?.cleanUp()
        val decorView = activity.window.decorView as ViewGroup
        toolTipViews.forEach {
            decorView.removeView(it)
        }
        toolTipViews.clear()
    }

    fun clean(view: View) {
        val viewHole = highlightedViews.firstOrNull { it.view === view }
        if (viewHole != null) {
            highlightedViews.remove(viewHole)
        }
        if (highlightedViews.isNotEmpty()) {
            reset()
            startView(false)
        } else {
            cleanUp()
        }
    }

    //TODO: move into Pointer
    private fun getXBasedOnGravity(width: Int, highlightedView: View, pointer: Pointer): Int {
        val x = highlightedView.locationOnScreen.x
        return when {
            pointer.gravity and Gravity.END == Gravity.END -> x + highlightedView.width - width
            pointer.gravity and Gravity.START == Gravity.START -> x
            else -> x + highlightedView.width / 2 - width / 2
        }
    }

    //TODO: move into Pointer
    private fun getYBasedOnGravity(height: Int, highlightedView: View, pointer: Pointer): Int {
        val y = highlightedView.locationOnScreen.y
        return when {
            pointer.gravity and Gravity.BOTTOM == Gravity.BOTTOM -> y + highlightedView.height - height
            pointer.gravity and Gravity.TOP == Gravity.TOP -> y
            else -> y + highlightedView.height / 2 - height / 2
        }
    }

    protected fun setupView() {
        // TourGuide can only be setup after all the views is ready and obtain it's position/measurement
        // so when this is the 1st time TourGuide is being added,
        // else block will be executed, and ViewTreeObserver will make TourGuide setup process to be delayed until everything is ready
        // when this is run the 2nd or more times, if block will be executed
        var isReady = true
        highlightedViews.forEach {
            it.view.let { highlightedView ->
                if (!ViewCompat.isAttachedToWindow(highlightedView)) {
                    isReady = false
                    pendingCount++
                    val viewTreeObserver = highlightedView.viewTreeObserver
                    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                highlightedView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                            } else {
                                highlightedView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            }
                            pendingCount--
                            if (pendingCount == 0) {
                                startView()
                            }
                        }
                    })
                }
            }
        }
        if (isReady) {
            startView()
        }
    }

    private fun startView(performToolTipStartAnimation: Boolean = true) {
        /* Initialize a frame layout with a hole */
        frameLayoutWithHole = FrameLayoutWithHole(activity, highlightedViews, overlay)
        /* handle click disable */
        frameLayoutWithHole?.let {
            handleDisableClicking(it)
        }

        /* setup floating action button */
        highlightedViews.forEach { highlightedView ->
            val config = highlightedView.config
            config.pointer?.let { pointer ->
                frameLayoutWithHole?.let {
                    val fab = setupAndAddFABToFrameLayout(it, highlightedView.view, pointer)
                    performAnimationOn(fab, pointer.technique, config.toolTip)
                }
            }
        }
        setupFrameLayout()
        /* setup tourguide_tooltip view */
        setupToolTip(performToolTipStartAnimation)
    }

    private fun handleDisableClicking(frameLayoutWithHole: FrameLayoutWithHole) {
        overlay?.also { _overlay ->
            if (_overlay.mOnClickListener != null) {
                // 1. if user provides an overlay listener, use that as 1st priority
                frameLayoutWithHole.setOnClickListener(_overlay.mOnClickListener)
            } else if (_overlay.mDisableClick) {
                // 2. if overlay listener is not provided, check if it's disabled
                Log.w("tourguide", "Overlay's default OnClickListener is null, it will proceed to next tourguide when it is clicked")
                frameLayoutWithHole.setViewHoles(highlightedViews)
                frameLayoutWithHole.isSoundEffectsEnabled = false
                frameLayoutWithHole.setOnClickListener { }
            }
        }
    }

    private fun setupToolTip(performStartAnimation: Boolean) {
        /* inflate and get views */
        toolTipViews.clear()
        val parent = activity.window.decorView as ViewGroup
        val layoutInflater = activity.layoutInflater
        highlightedViews.forEach {
            it.config.toolTip?.let { _toolTip ->
                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                val highlightedView = it.view
                val toolTipView = _toolTip.getCustomView()
                        ?: _toolTip.getDefaultToolTipView(layoutInflater)
                toolTipView.also { _toolTipView ->
                    if (performStartAnimation) {
                        _toolTipView.startAnimation(_toolTip.mEnterAnimation)
                    }

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
                        resultPoint.x = getXForTooTip(_toolTip.mGravity, parent.width, targetViewX, adjustment, highlightedView)
                    } else {
                        resultPoint.x = getXForTooTip(_toolTip.mGravity, toolTipMeasuredWidth, targetViewX, adjustment, highlightedView)
                    }

                    resultPoint.y = getYForTooTip(_toolTip.mGravity, toolTipMeasuredHeight, targetViewY, adjustment, highlightedView)

                    parent.addView(_toolTipView, layoutParams)
                    toolTipViews.add(_toolTipView)

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
                            fixedY = getYForTooTip(_toolTip.mGravity, toolTipHeightAfterLayouted, targetViewY, adjustment, highlightedView)
                            layoutParams.setMargins(_toolTipView.x.toInt(), fixedY, 0, 0)
                        }
                    })

                    // set the position using setMargins on the left and top
                    layoutParams.setMargins(resultPoint.x, resultPoint.y, 0, 0)
                }
            }
        }
    }

    private fun getXForTooTip(gravity: Int, toolTipMeasuredWidth: Int, targetViewX: Int, adjustment: Float, highlightedView: View) =
            when {
                gravity and Gravity.START == Gravity.START -> targetViewX - toolTipMeasuredWidth + adjustment.toInt()
                gravity and Gravity.END == Gravity.END -> targetViewX + highlightedView.width - adjustment.toInt()
                else -> targetViewX + highlightedView.width / 2 - toolTipMeasuredWidth / 2
            }

    private fun getYForTooTip(gravity: Int, toolTipMeasuredHeight: Int, targetViewY: Int, adjustment: Float, highlightedView: View) =
            when {
                gravity and Gravity.TOP == Gravity.TOP -> if (gravity and Gravity.START == Gravity.START || gravity and Gravity.END == Gravity.END) {
                    targetViewY - toolTipMeasuredHeight + adjustment.toInt()
                } else {
                    targetViewY - toolTipMeasuredHeight - adjustment.toInt()
                }
                else -> // this is center
                    if (gravity and Gravity.START == Gravity.START || gravity and Gravity.END == Gravity.END) {
                        targetViewY + highlightedView.height - adjustment.toInt()
                    } else {
                        targetViewY + highlightedView.height + adjustment.toInt()
                    }
            }

    private fun setupAndAddFABToFrameLayout(frameLayoutWithHole: FrameLayoutWithHole, highlightedView: View, pointer: Pointer): FloatingActionButton {
        // invisFab is invisible, and it's only used for getting the width and height
        val invisFab = FloatingActionButton(activity)
        invisFab.size = FloatingActionButton.SIZE_MINI
        invisFab.visibility = View.INVISIBLE
        (activity.window.decorView as ViewGroup).addView(invisFab)

        // fab is the real fab that is going to be added
        return FloatingActionButton(activity).apply {
            setBackgroundColor(Color.BLUE)
            size = FloatingActionButton.SIZE_MINI
            colorNormal = pointer.color
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
                    params.setMargins(
                            getXBasedOnGravity(invisFab.width, highlightedView, pointer),
                            getYBasedOnGravity(invisFab.height, highlightedView, pointer),
                            0,
                            0
                    )
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

    private fun performAnimationOn(view: View, technique: Pointer.Technique, toolTip: ToolTip?) {
        when (technique) {
            Pointer.Technique.HORIZONTAL_LEFT -> {
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
            }
            Pointer.Technique.HORIZONTAL_RIGHT -> {
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
                val goRightXDuration: Long = 2000
                val translationX = (screenWidth / 2).toFloat()

                val fadeInAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
                fadeInAnim.duration = fadeInDuration
                val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f)
                scaleDownX.duration = scaleDownDuration
                val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f)
                scaleDownY.duration = scaleDownDuration
                val goRightX = ObjectAnimator.ofFloat(view, "translationX", translationX)
                goRightX.duration = goRightXDuration
                val fadeOutAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
                fadeOutAnim.duration = goRightXDuration

                val fadeInAnim2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
                fadeInAnim2.duration = fadeInDuration
                val scaleDownX2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f)
                scaleDownX2.duration = scaleDownDuration
                val scaleDownY2 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f)
                scaleDownY2.duration = scaleDownDuration
                val goRightX2 = ObjectAnimator.ofFloat(view, "translationX", translationX)
                goRightX2.duration = goRightXDuration
                val fadeOutAnim2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
                fadeOutAnim2.duration = goRightXDuration

                animatorSet.play(fadeInAnim)
                animatorSet.play(scaleDownX).with(scaleDownY).after(fadeInAnim)
                animatorSet.play(goRightX).with(fadeOutAnim).after(scaleDownY)

                animatorSet2.play(fadeInAnim2)
                animatorSet2.play(scaleDownX2).with(scaleDownY2).after(fadeInAnim2)
                animatorSet2.play(goRightX2).with(fadeOutAnim2).after(scaleDownY2)

                animatorSet.addListener(lis1)
                animatorSet2.addListener(lis2)
                animatorSet.start()

                /* these animatorSets are kept track in FrameLayout, so that they can be cleaned up when FrameLayout is detached from window */
                frameLayoutWithHole?.addAnimatorSet(animatorSet)
                frameLayoutWithHole?.addAnimatorSet(animatorSet2)
            }
            Pointer.Technique.VERTICAL_UPWARD -> {
                val animatorSet = AnimatorSet()
                val animatorSet2 = AnimatorSet()
                val lis1 = object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {
                        view.scaleX = 1f
                        view.scaleY = 1f
                        view.translationY = 0f
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
                        view.translationY = 0f
                        animatorSet.start()
                    }
                }

                val fadeInDuration: Long = 800
                val scaleDownDuration: Long = 800
                val goUpYDuration: Long = 2000
                val translationY = (screenHeight / 4).toFloat()

                val fadeInAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
                fadeInAnim.duration = fadeInDuration
                val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f)
                scaleDownX.duration = scaleDownDuration
                val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f)
                scaleDownY.duration = scaleDownDuration
                val goUpY = ObjectAnimator.ofFloat(view, "translationY", -translationY)
                goUpY.duration = goUpYDuration
                val fadeOutAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
                fadeOutAnim.duration = goUpYDuration

                val fadeInAnim2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
                fadeInAnim2.duration = fadeInDuration
                val scaleDownX2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f)
                scaleDownX2.duration = scaleDownDuration
                val scaleDownY2 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f)
                scaleDownY2.duration = scaleDownDuration
                val goUpY2 = ObjectAnimator.ofFloat(view, "translationY", -translationY)
                goUpY2.duration = goUpYDuration
                val fadeOutAnim2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
                fadeOutAnim2.duration = goUpYDuration

                animatorSet.play(fadeInAnim)
                animatorSet.play(scaleDownX).with(scaleDownY).after(fadeInAnim)
                animatorSet.play(goUpY).with(fadeOutAnim).after(scaleDownY)

                animatorSet2.play(fadeInAnim2)
                animatorSet2.play(scaleDownX2).with(scaleDownY2).after(fadeInAnim2)
                animatorSet2.play(goUpY2).with(fadeOutAnim2).after(scaleDownY2)

                animatorSet.addListener(lis1)
                animatorSet2.addListener(lis2)
                animatorSet.start()

                /* these animatorSets are kept track in FrameLayout, so that they can be cleaned up when FrameLayout is detached from window */
                frameLayoutWithHole?.addAnimatorSet(animatorSet)
                frameLayoutWithHole?.addAnimatorSet(animatorSet2)
            }
            Pointer.Technique.VERTICAL_DOWNWARD -> {
                val animatorSet = AnimatorSet()
                val animatorSet2 = AnimatorSet()
                val lis1 = object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {
                        view.scaleX = 1f
                        view.scaleY = 1f
                        view.translationY = 0f
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
                        view.translationY = 0f
                        animatorSet.start()
                    }
                }

                val fadeInDuration: Long = 800
                val scaleDownDuration: Long = 800
                val goDownYDuration: Long = 2000
                val translationY = (screenHeight / 4).toFloat()

                val fadeInAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
                fadeInAnim.duration = fadeInDuration
                val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f)
                scaleDownX.duration = scaleDownDuration
                val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f)
                scaleDownY.duration = scaleDownDuration
                val goDownY = ObjectAnimator.ofFloat(view, "translationY", translationY)
                goDownY.duration = goDownYDuration
                val fadeOutAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
                fadeOutAnim.duration = goDownYDuration

                val fadeInAnim2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
                fadeInAnim2.duration = fadeInDuration
                val scaleDownX2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f)
                scaleDownX2.duration = scaleDownDuration
                val scaleDownY2 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f)
                scaleDownY2.duration = scaleDownDuration
                val goDownY2 = ObjectAnimator.ofFloat(view, "translationY", translationY)
                goDownY2.duration = goDownYDuration
                val fadeOutAnim2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
                fadeOutAnim2.duration = goDownYDuration

                animatorSet.play(fadeInAnim)
                animatorSet.play(scaleDownX).with(scaleDownY).after(fadeInAnim)
                animatorSet.play(goDownY).with(fadeOutAnim).after(scaleDownY)

                animatorSet2.play(fadeInAnim2)
                animatorSet2.play(scaleDownX2).with(scaleDownY2).after(fadeInAnim2)
                animatorSet2.play(goDownY2).with(fadeOutAnim2).after(scaleDownY2)

                animatorSet.addListener(lis1)
                animatorSet2.addListener(lis2)
                animatorSet.start()

                /* these animatorSets are kept track in FrameLayout, so that they can be cleaned up when FrameLayout is detached from window */
                frameLayoutWithHole?.addAnimatorSet(animatorSet)
                frameLayoutWithHole?.addAnimatorSet(animatorSet2)
            }
            else -> { // do click for default case
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
    }

    fun overlay(block: Overlay.() -> Unit) {
        overlay = Overlay().apply { block() }
    }

    companion object {
        @JvmStatic
        open fun init(activity: Activity) = TourGuide(activity)

        fun create(activity: Activity, block: TourGuide.() -> Unit = {}): TourGuide {
            return TourGuide(activity).apply { block() }
        }
    }
}
