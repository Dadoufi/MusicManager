package dadoufi.musicmanager.ui.album

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.internal.CheckableImageButton
import dadoufi.musicmanager.R
import dadoufi.musicmanager.extensions.isValidContextForGlide
import dadoufi.musicmanager.util.DebouncedOnClickListener
import dadoufi.musicmanager.util.epoxy.KotlinEpoxyHolder
import dadoufi.musicmanager.util.glide.GlideApp

@SuppressLint("RestrictedApi")
@EpoxyModelClass(layout = R.layout.item_album, useLayoutOverloads = true)
abstract class AlbumModel : EpoxyModelWithHolder<AlbumHolder>() {


    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: DebouncedOnClickListener
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var saveClickListener: () -> Unit


    @EpoxyAttribute
    lateinit var albumName: String
    @EpoxyAttribute
    lateinit var artistName: String
    @EpoxyAttribute
    lateinit var albumIcon: String
    @EpoxyAttribute
    var favorite: Boolean = false

    override fun bind(holder: AlbumHolder) {
        with(holder) {

            titleView.text = albumName

            artistView.text = artistName
            GlideApp.with(imageView)
                .load(albumIcon)
                .rounded()
                .into(imageView)

            if (saveView is CheckableImageButton) {
                (saveView as CheckableImageButton).isChecked = favorite
                saveView.setOnClickListener {
                    saveClickListener()
                }
            }

            ViewCompat.setTransitionName(
                imageView, "image${(artistName + albumName).hashCode()}"
            )


            holder.parent.setOnClickListener(clickListener as View.OnClickListener)
        }

    }

    @Override
    @LayoutRes
    override fun getDefaultLayout(): Int {
        return R.layout.item_album
    }


    override fun bind(holder: AlbumHolder, previouslyBoundModel: EpoxyModel<*>) {

        if (previouslyBoundModel is AlbumModel && previouslyBoundModel.favorite != favorite) {
            (holder.saveView as CheckableImageButton).isChecked = favorite
        } else {
            super.bind(holder, previouslyBoundModel)
        }
    }


    override fun unbind(holder: AlbumHolder) {
        // Release resources and don't leak listeners as this view goes back to the view pool
        holder.saveView.setOnClickListener(null)
        holder.parent.setOnClickListener(null)

        if (holder.imageView.context.isValidContextForGlide()) {
            GlideApp.with(holder.imageView.context).clear(holder.imageView)
        }

    }


}

class AlbumHolder : KotlinEpoxyHolder() {
    val imageView by bind<ImageView>(R.id.image)
    val titleView by bind<TextView>(R.id.title)
    val artistView by bind<TextView>(R.id.artist)
    val saveView by bind<ImageView>(R.id.save)
    val parent by bind<ConstraintLayout>(R.id.parent)

}



