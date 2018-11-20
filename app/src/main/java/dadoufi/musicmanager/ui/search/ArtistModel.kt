package dadoufi.musicmanager.ui.search

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import dadoufi.musicmanager.R
import dadoufi.musicmanager.data.entities.ArtistEntity
import dadoufi.musicmanager.extensions.isValidContextForGlide
import dadoufi.musicmanager.util.DebouncedOnClickListener
import dadoufi.musicmanager.util.epoxy.KotlinEpoxyHolder
import dadoufi.musicmanager.util.glide.GlideApp


@EpoxyModelClass(layout = R.layout.item_artist)
abstract class ArtistModel : EpoxyModelWithHolder<ArtistHolder>() {


    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: DebouncedOnClickListener
    @EpoxyAttribute
    var item: ArtistEntity? = null

    override fun bind(holder: ArtistHolder) {
        with(holder) {
            item?.let {

                titleView.text = it.name

                GlideApp.with(imageView)
                    .load(it.icon)
                    .roundedArtist()
                    .into(imageView)

                ViewCompat.setTransitionName(imageView, "image${it.hashCode()}")

                parent.setOnClickListener(clickListener)

            }

        }


    }


    override fun unbind(holder: ArtistHolder) {
        // Release resources and don't leak listeners as this view goes back to the view pool
        holder.parent.setOnClickListener(null)

        if (holder.imageView.context.isValidContextForGlide()) {
            GlideApp.with(holder.imageView.context).clear(holder.imageView)
        }

    }


}

class ArtistHolder : KotlinEpoxyHolder() {
    val imageView by bind<ImageView>(R.id.image)
    val titleView by bind<TextView>(R.id.title)
    val parent by bind<ConstraintLayout>(R.id.search_parent)

}