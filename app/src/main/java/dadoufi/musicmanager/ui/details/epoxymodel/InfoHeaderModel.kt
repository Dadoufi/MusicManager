package dadoufi.musicmanager.ui.details.epoxymodel

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import dadoufi.musicmanager.R
import dadoufi.musicmanager.data.entities.AlbumDetailsEntity
import dadoufi.musicmanager.util.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_album_info_header)
abstract class InfoHeaderModel : EpoxyModelWithHolder<InfoHeaderHolder>() {

    @EpoxyAttribute
    var item: AlbumDetailsEntity? = null
    @EpoxyAttribute
    var trackCount = 0
    @EpoxyAttribute
    var totalDuration: String? = null

    override fun bind(holder: InfoHeaderHolder) {
        with(holder) {
            item?.let {

                title.text = it.albumName
                artistName.text = it.artistName

                albumInformation.text = holder.albumInformation.context.getString(
                    R.string.album_info,
                    it.published,
                    trackCount,
                    totalDuration,
                    it.listeners,
                    it.playCount
                )
            }
        }
    }


}

class InfoHeaderHolder : KotlinEpoxyHolder() {
    val title by bind<TextView>(R.id.infoTitle)
    val artistName by bind<TextView>(R.id.infoArtistName)
    val albumInformation by bind<TextView>(R.id.albumInformation)
}