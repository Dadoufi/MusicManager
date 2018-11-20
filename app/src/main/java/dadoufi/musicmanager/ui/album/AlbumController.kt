package dadoufi.musicmanager.ui.album

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyModel
import dadoufi.musicmanager.R
import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.util.DebouncedOnClickListener
import dadoufi.musicmanager.util.epoxy.BaseController
import dadoufi.musicmanager.util.epoxy.common.EmptyModel_
import dadoufi.musicmanager.util.epoxy.common.HeaderModel_
import dadoufi.musicmanager.util.epoxy.common.LoadingModel_
import dadoufi.musicmanager.util.epoxy.common.RetryModel_


class AlbumController(
    private val callbacks: Callbacks,
    private val title: String? = null,
    private val isFavorite: Boolean = false,
    private val context: Context? = null
) :
    BaseController<AlbumEntity>(
        diffUtilCallback = object : DiffUtil.ItemCallback<AlbumEntity>() {
            override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean =
                oldItem.id == newItem.id
                        && oldItem.favorite == newItem.favorite && oldItem.albumName == newItem.albumName
        }
    ) {


    @AutoModel
    lateinit var loadingState: LoadingModel_
    @AutoModel
    lateinit var emptyState: EmptyModel_
    @AutoModel
    lateinit var header: HeaderModel_
    @AutoModel
    lateinit var retryState: RetryModel_


    override fun toggleLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    override fun toggleRetry(retry: Boolean) {
        this.retry = retry
    }

    init {
        isDebugLoggingEnabled = true
    }


    override fun buildItemModel(currentPosition: Int, item: AlbumEntity?): EpoxyModel<*> {
        item?.let {
            return AlbumModel_()
                .layout(if (isFavorite) R.layout.item_album_favorite else R.layout.item_album)
                .id(item.mid)
                .albumName(item.albumName.orEmpty())
                .albumIcon(item.albumIcon.orEmpty())
                .artistName(item.artistName.orEmpty())
                .favorite(item.favorite)
                .clickListener(object : DebouncedOnClickListener(1000) {
                    override fun onDebouncedClick(view: View) {
                        callbacks.onItemClicked(item, view.findViewById(R.id.image))
                    }
                })
                .saveClickListener {
                    if (callbacks is TopAlbumCallbacks) callbacks.onSaveClicked(
                        item,
                        currentPosition
                    )
                }

        } ?: run {
            return AlbumModel_()
                .id(currentPosition)
        }
    }


    override fun addModels(models: List<EpoxyModel<*>>) {

        if (title != null) {
            header
                .listHeader(title)
                .addTo(this)
        } /*else if (isFavorite && !models.isNullOrEmpty()) {
            header.layout(R.layout.item_header_tracks)
                .listHeader(
                    context?.resources?.getQuantityString(
                        R.plurals.number_of_favorite_albums,
                        models.count(),
                        models.count()
                    )
                )
                .addTo(this)
        }*/

        emptyState
            .emptyText(context?.getString(R.string.empty_favorites))
            .addIf(models.isNullOrEmpty(), this)


        super.addModels(models)

        loadingState
            .addIf(
                isLoading, this
            )

        retryState
            .error("Opps something went wrong")
            .retryClickListener { if (callbacks is TopAlbumCallbacks) callbacks.retryClicked() }
            .addIf(
                retry, this
            )
    }


    override fun onExceptionSwallowed(exception: RuntimeException) {
        throw exception
    }


    interface Callbacks {
        fun onItemClicked(item: AlbumEntity, sharedElementImageView: ImageView)
    }

    interface TopAlbumCallbacks : Callbacks {
        fun retryClicked()
        fun onSaveClicked(item: AlbumEntity, position: Int)
    }


}





