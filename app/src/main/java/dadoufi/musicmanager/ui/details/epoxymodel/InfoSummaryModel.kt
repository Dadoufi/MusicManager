package dadoufi.musicmanager.ui.details.epoxymodel

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import dadoufi.musicmanager.R
import dadoufi.musicmanager.data.entities.AlbumDetailsEntity
import dadoufi.musicmanager.util.MaxLinesToggleClickListener
import dadoufi.musicmanager.util.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_album_info_summary)
abstract class InfoSummaryModel : EpoxyModelWithHolder<InfoSummaryHolder>() {

    @EpoxyAttribute
    var item: AlbumDetailsEntity? = null

    override fun bind(holder: InfoSummaryHolder) {
        with(holder) {
            item?.let {

                with(summary) {
                    it.info?.let { info ->
                        text = info
                    }
                    setOnClickListener(MaxLinesToggleClickListener(3))
                }

            }
        }


    }


}

class InfoSummaryHolder : KotlinEpoxyHolder() {
    val summary by bind<TextView>(R.id.summary)
}