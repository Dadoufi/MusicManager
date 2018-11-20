package dadoufi.musicmanager.ui.search

import android.view.View
import android.widget.ImageView
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyModel
import dadoufi.musicmanager.R
import dadoufi.musicmanager.data.entities.ArtistEntity
import dadoufi.musicmanager.util.DebouncedOnClickListener
import dadoufi.musicmanager.util.epoxy.BaseController
import dadoufi.musicmanager.util.epoxy.common.EmptyModel_
import dadoufi.musicmanager.util.epoxy.common.LoadingModel_
import dadoufi.musicmanager.util.epoxy.common.RetryModel_

class ArtistController(private val callbacks: Callbacks) :
    BaseController<ArtistEntity>() {


    @AutoModel
    lateinit var loadingState: LoadingModel_
    @AutoModel
    lateinit var emptyState: EmptyModel_
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


    override fun buildItemModel(currentPosition: Int, item: ArtistEntity?): EpoxyModel<*> {
        item?.let {
            return ArtistModel_()
                .id(item.hashCode())
                .item(item)
                .clickListener(object : DebouncedOnClickListener(1000) {
                    override fun onDebouncedClick(view: View) {
                        callbacks.onItemClicked(item, view.findViewById(R.id.image))
                    }
                })

        } ?: run {
            return ArtistModel_()
                .id(currentPosition)
        }
    }


    override fun addModels(models: List<EpoxyModel<*>>) {
        super.addModels(models)
        loadingState
            .addIf(
                isLoading, this
            )

        retryState
            .error("Opps something went wrong")
            .retryClickListener { callbacks.retryClicked() }
            .addIf(
                retry, this
            )

    }


    override fun onExceptionSwallowed(exception: RuntimeException) {
        throw exception
    }


    interface Callbacks {
        fun onItemClicked(item: ArtistEntity, sharedElementImageView: ImageView)
        fun retryClicked()
    }


}