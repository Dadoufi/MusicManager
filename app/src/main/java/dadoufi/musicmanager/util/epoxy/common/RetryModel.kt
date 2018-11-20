package dadoufi.musicmanager.util.epoxy.common

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.button.MaterialButton
import dadoufi.musicmanager.R
import dadoufi.musicmanager.util.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_retry)
abstract class RetryModel : EpoxyModelWithHolder<RetryHolder>() {


    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var retryClickListener: () -> Unit

    @EpoxyAttribute
    lateinit var error: String

    override fun bind(holder: RetryHolder) {
        holder.errorText.text = error
        holder.retry.setOnClickListener { retryClickListener() }

    }


    override fun unbind(holder: RetryHolder) {
        super.unbind(holder)
        holder.retry.setOnClickListener(null)
    }
}


class RetryHolder : KotlinEpoxyHolder() {
    val errorText by bind<TextView>(R.id.errorText)
    val retry by bind<MaterialButton>(R.id.retry)
}