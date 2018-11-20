package dadoufi.musicmanager.util.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import dadoufi.musicmanager.R
import dadoufi.musicmanager.extensions.animatedDrawable
import dadoufi.musicmanager.extensions.dimension

class ToolbarButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var maxAlpha = 1f

    init {
        val padding = context.dimension(R.dimen.toolbar_action_button_padding)
        setPadding(padding, padding, padding, padding)
        val outValue = TypedValue()
        context.theme.resolveAttribute(
            android.R.attr.selectableItemBackgroundBorderless,
            outValue,
            true
        )
        //if (background != null) {
        // setImageResource(outValue.resourceId)
        // }
    }

    /* override fun setImageDrawable(drawable: Drawable?) {
         //drawable?.setTint(context.obtainColor(android.R.attr.textColorSecondary))
         super.setImageDrawable(drawable)
     }*/

    fun setAnimatedImageDrawable(@DrawableRes drawableId: Int) {
        val drawable = context.animatedDrawable(drawableId)
        super.setImageDrawable(drawable)
        (this.drawable as AnimatedVectorDrawableCompat).start()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        maxAlpha = if (enabled) 1f else 0.5f
        alpha = 1f
    }

    override fun setAlpha(alpha: Float) {
        super.setAlpha(alpha * maxAlpha)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
            if (marginStart == 0) marginStart = context.dimension(R.dimen.margin_very_small)
        }
        (parent as View).let { parent ->
            val extraTouchArea = context.dimension(R.dimen.toolbar_action_button_extra_touch_area)
            val bounds = Rect()
            getHitRect(bounds)
            bounds.left -= extraTouchArea
            bounds.top -= extraTouchArea
            bounds.right += extraTouchArea
            bounds.bottom += extraTouchArea
            //TODO: Does not seem to be working.
            parent.touchDelegate = (parent.touchDelegate as? TouchDelegateComposite) ?:
                    TouchDelegateComposite(this).apply {
                        addDelegate(TouchDelegate(bounds, this@ToolbarButton))
                    }
        }
    }

    private inner class TouchDelegateComposite(view: View) : TouchDelegate(Rect(), view) {

        private val delegates = mutableListOf<TouchDelegate>()

        override fun onTouchEvent(event: MotionEvent): Boolean {
            var res = false
            val x = event.x
            val y = event.y
            for (delegate in delegates) {
                event.setLocation(x, y)
                res = delegate.onTouchEvent(event) || res
            }
            return res
        }

        fun addDelegate(delegate: TouchDelegate) = delegates.add(delegate)
    }
}