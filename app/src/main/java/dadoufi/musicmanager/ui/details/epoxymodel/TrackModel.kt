package dadoufi.musicmanager.ui.details.epoxymodel

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import dadoufi.musicmanager.R
import dadoufi.musicmanager.data.entities.TrackEntity
import dadoufi.musicmanager.extensions.asDuration
import dadoufi.musicmanager.util.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_track)
abstract class TrackModel : EpoxyModelWithHolder<TrackHolder>() {

    @EpoxyAttribute
    var item: TrackEntity? = null
    @EpoxyAttribute
    var number: Int = 0

    override fun bind(holder: TrackHolder) {
        with(holder) {
            item?.let {

                trackNumberView.text = number.toString()
                titleView.text = it.name

                durationView.text = it.duration?.toInt()?.asDuration()
            }
        }
    }


}

class TrackHolder : KotlinEpoxyHolder() {
    val trackNumberView by bind<TextView>(R.id.trackNumber)
    val titleView by bind<TextView>(R.id.trackTitle)
    val durationView by bind<TextView>(R.id.duration)
}