package dadoufi.musicmanager.util.widget

import android.content.Context
import android.os.Parcelable
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import dadoufi.musicmanager.R
import dadoufi.musicmanager.extensions.*
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.view_toolbar_text_input.view.*


class ToolbarTextInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var searchViewEditText: EditText


    init {
        setup()
        searchViewEditText = findViewById(R.id.searchView)
        clipChildren = false
    }

    private fun setup() {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_toolbar_text_input, this, true)

        searchView.run {
            setText("")
            hint = context.getString(R.string.search_hint)
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            filters = arrayOfNulls<InputFilter>(1).apply {
                this[0] =
                        InputFilter.LengthFilter(context.resources.getInteger(R.integer.search_query_limit))
                visibleOrInvisible = false
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == imeOptions) {
                    hideKeyboard(this)
                    onDoneButtonPressed()
                }
                true
            }

        }

        erase.run {
            erase.isEnabled = false
            setOnClickListener {
                searchViewEditText.text.clear()
                erase.isEnabled = false
            }
        }

        back.setOnClickListener {
            hideKeyboard(searchView)
            onBackButtonPressed()
        }

        search.setOnClickListener {
            toggleSearchVisibility()
        }

    }

    fun toggleErase(editable: String) {
        erase.isEnabled = !editable.isBlank()
    }

    fun toggleSearchVisibility() {
        animateTextInputVisibility(!searchView.visibleOrGone)
        search.setAnimatedImageDrawable(if (isTextInputVisible) R.drawable.avd_search_to_close else R.drawable.avd_close_to_search)
    }


    var onDoneButtonPressed: () -> Unit = {

    }
    var onBackButtonPressed: () -> Unit = {}


    var isTextInputVisible = false


    private fun animateTextInputVisibility(isVisible: Boolean) {
        if (isTextInputVisible != isVisible) {
            isTextInputVisible = isVisible
            searchView.animatedVisibilityEnd = isVisible
            TransitionManager.beginDelayedTransition(this)
            erase.visibleOrGone = isVisible
            if (isVisible) {
                searchView.requestFocus()
                showKeyboard(searchView)
            } else {
                hideKeyboard(searchView)
            }
        }
    }

    private fun showTextInput() {
        erase.isEnabled = !searchView.text.isNullOrBlank()
        erase.visibleOrGone = searchView.text.isNullOrBlank()

        searchView.visibleOrInvisible = true
        isTextInputVisible = true
    }

    override fun onSaveInstanceState(): Parcelable =
        State(super.onSaveInstanceState(), isTextInputVisible)

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? State)?.let {
            super.onRestoreInstanceState(it.state)
            if (it.isTextInputVisible) {
                showTextInput()
                onDoneButtonPressed()

            }
        } ?: super.onRestoreInstanceState(state)
    }

    @Parcelize
    private data class State(val state: Parcelable?, val isTextInputVisible: Boolean) :
        BaseSavedState(state),
        Parcelable
}


