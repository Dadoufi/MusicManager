package dadoufi.musicmanager.extensions

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import dadoufi.musicmanager.R
import dadoufi.musicmanager.data.entities.UiState
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

fun Context?.isValidContextForGlide(): Boolean {
    if (this is Activity) {
        if (this.isDestroyed || this.isFinishing) {
            return false
        }
    }
    return true
}

fun showKeyboard(focusedView: View?) = focusedView?.also {
    it.requestFocus()
    (it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
        it,
        0
    )
}

fun hideKeyboard(focusedView: View?) = focusedView?.also {
    it.clearFocus()
    (it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        it.windowToken,
        0
    )
}

var View.visibleOrGone
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

var View.visibleOrInvisible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }

var View.animatedVisibilityStart: Boolean
    get() = visibleOrGone
    set(value) {
        animateCircularReveal(value, true)
    }

var View.animatedVisibilityEnd: Boolean
    get() = visibleOrGone
    set(value) {
        animateCircularReveal(value, false)
    }


fun Context.color(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)

fun Context.colorStateList(@ColorRes colorId: Int) = ContextCompat.getColorStateList(this, colorId)

fun Context.dimension(@DimenRes dimensionId: Int) = resources.getDimensionPixelSize(dimensionId)

fun Context.drawable(@DrawableRes drawableId: Int) =
    AppCompatResources.getDrawable(this, drawableId)

fun Context.font(@FontRes fontId: Int) =
    ResourcesCompat.getFont(this, fontId) ?: throw(Throwable("Font doesn't exist"))

fun Context.animatedDrawable(@DrawableRes drawableId: Int) =
    AnimatedVectorDrawableCompat.create(this, drawableId)


@ColorInt
fun Context.obtainColor(@AttrRes colorAttribute: Int): Int {
    val attributes = obtainStyledAttributes(TypedValue().data, intArrayOf(colorAttribute))
    val color = attributes.getColor(0, 0)
    attributes.recycle()
    return color
}


fun View.animateCircularReveal(isVisible: Boolean, start: Boolean) {
    if (isAttachedToWindow) {
        val cx = if (start) 0 else width
        val cy = height / 2
        val maxRadius = Math.hypot(width.toDouble(), height.toDouble()).toFloat()
        visibleOrGone = true
        val animator = ViewAnimationUtils.createCircularReveal(
            this,
            cx,
            cy,
            if (isVisible) 0f else maxRadius,
            if (isVisible) maxRadius else 0f
        ).apply {
            addListener(onAnimationEnd = {
                visibleOrGone = isVisible
                tag = null
            })
        }
        tag = animator
        animator.start()
    }
}

fun Int.asDuration(type: TimeUnit = TimeUnit.MINUTES): String {

    val timeInSeconds = this

    val df = DecimalFormat("00")

    val hours = (timeInSeconds / 3600)
    var remaining = (timeInSeconds % 3600)
    val minutes = remaining / 60
    remaining %= 60
    val seconds = remaining

    var text = ""
    if (TimeUnit.HOURS == type) {
        text += df.format(hours) + ":"
    }
    text += df.format(minutes) + ":"
    text += df.format(seconds)

    return text

}


inline fun Animator.addListener(
    crossinline onAnimationRepeat: () -> Unit = {},
    crossinline onAnimationEnd: () -> Unit = {},
    crossinline onAnimationCancel: () -> Unit = {},
    crossinline onAnimationStart: () -> Unit = {}
) = addListener(object : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) = onAnimationRepeat()

    override fun onAnimationEnd(animation: Animator?) = onAnimationEnd()

    override fun onAnimationCancel(animation: Animator?) = onAnimationCancel()

    override fun onAnimationStart(animation: Animator?) = onAnimationStart()
})

inline fun EditText.onTextChanged(crossinline callback: (String) -> Unit) =
    addTextChangedListener(object :
        TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
            callback(s?.toString() ?: "")
    })

fun Snackbar.config(context: Context) {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    this.view.layoutParams = params

    this.view.background = context.getDrawable(R.drawable.bg_snackbar)

    ViewCompat.setElevation(this.view, 6f)
}

fun Fragment.createSnackBar(
    parentView: View,
    error: UiState.Error.RefreshError,
    duration: Int = LENGTH_LONG,
    actionClickListener: () -> Unit
): Snackbar {
    return Snackbar.make(parentView, error.message.toString(), duration).apply {
        setAction(R.string.retry) {
            actionClickListener()
        }
        config(view.context)
    }

}

fun RecyclerView.clearDecorations() {
    if (itemDecorationCount > 0) {
        (itemDecorationCount - 1 downTo 0)
            .filter { getItemDecorationAt(it) !is ItemTouchHelper } //do not remove itemTouchHelper so we can swipe to deleteCompletable
            .forEach { i ->
                removeItemDecorationAt(i)
            }
    }

}




