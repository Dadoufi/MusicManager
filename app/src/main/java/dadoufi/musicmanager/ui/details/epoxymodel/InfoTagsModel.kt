package dadoufi.musicmanager.ui.details.epoxymodel

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dadoufi.musicmanager.R
import dadoufi.musicmanager.data.entities.AlbumDetailsEntity
import dadoufi.musicmanager.util.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_album_info_tags)
abstract class InfoTagsModel : EpoxyModelWithHolder<InfoTagsHeaderHolder>() {

    @EpoxyAttribute
    var item: AlbumDetailsEntity? = null

    override fun bind(holder: InfoTagsHeaderHolder) {
        with(holder) {
            item?.let {


                if (it.tags.size != chipGroup.childCount) {
                    chipGroup.removeAllViews()
                }
                if (chipGroup.childCount == 0) {
                    it.tags.forEach { tag ->
                        val chip = Chip(holder.chipGroup.context).apply {
                            text = tag

                        }

                        chipGroup.addView(chip)
                    }
                }
            }
        }
    }


}

class InfoTagsHeaderHolder : KotlinEpoxyHolder() {
    val chipGroup by bind<ChipGroup>(R.id.chipGroup)
}




