package dadoufi.musicmanager.util.epoxy.common

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import dadoufi.musicmanager.R
import dadoufi.musicmanager.util.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_header, useLayoutOverloads = true)
abstract class HeaderModel : EpoxyModelWithHolder<HeaderHolder>() {


    @EpoxyAttribute
    var listHeader: String? = null

    override fun bind(holder: HeaderHolder) {

        listHeader?.let {
            holder.titleHeaderView.text = listHeader
        }

    }
}


class HeaderHolder : KotlinEpoxyHolder() {
    val titleHeaderView by bind<TextView>(R.id.listHeader)
}