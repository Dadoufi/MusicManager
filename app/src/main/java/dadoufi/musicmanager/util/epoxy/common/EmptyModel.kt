package dadoufi.musicmanager.util.epoxy.common

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import dadoufi.musicmanager.R
import dadoufi.musicmanager.util.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_empty)
abstract class EmptyModel : EpoxyModelWithHolder<EmptyHolder>() {


    @EpoxyAttribute
    var emptyText: String? = null

    override fun bind(holder: EmptyHolder) {
        emptyText?.let {
            holder.empty.text = emptyText
        }


    }
}


class EmptyHolder : KotlinEpoxyHolder() {
    val empty by bind<TextView>(R.id.emptyText)
}
