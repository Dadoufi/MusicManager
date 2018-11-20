package dadoufi.musicmanager.ui.details

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed4EpoxyController
import dadoufi.musicmanager.R
import dadoufi.musicmanager.data.entities.AlbumDetailsEntity
import dadoufi.musicmanager.data.entities.TrackEntity
import dadoufi.musicmanager.ui.details.epoxymodel.InfoHeaderModel_
import dadoufi.musicmanager.ui.details.epoxymodel.InfoSummaryModel_
import dadoufi.musicmanager.ui.details.epoxymodel.InfoTagsModel_
import dadoufi.musicmanager.ui.details.epoxymodel.TrackModel_
import dadoufi.musicmanager.util.epoxy.common.EmptyModel_
import dadoufi.musicmanager.util.epoxy.common.HeaderModel_

class TracksController :
    Typed4EpoxyController<List<TrackEntity>, AlbumDetailsEntity, String, Context>() {


    @AutoModel
    lateinit var emptyState: EmptyModel_
    @AutoModel
    lateinit var infoHeader: InfoHeaderModel_
    @AutoModel
    lateinit var infoTags: InfoTagsModel_
    @AutoModel
    lateinit var infoSummary: InfoSummaryModel_
    @AutoModel
    lateinit var header: HeaderModel_


    init {
        isDebugLoggingEnabled = true
    }

    override fun onExceptionSwallowed(exception: RuntimeException) {
        throw exception
    }

    override fun buildModels(
        data: List<TrackEntity>?,
        albumDetailsEntity: AlbumDetailsEntity,
        totalDuration: String?,
        context: Context?
    ) {


        infoHeader
            .item(albumDetailsEntity)
            .trackCount(data?.count() ?: 0)
            .totalDuration(totalDuration)
            .addTo(this)

        infoTags.item(albumDetailsEntity)
            .addIf(!albumDetailsEntity.tags.isNullOrEmpty(), this)

        infoSummary.item(albumDetailsEntity)
            .addIf(!albumDetailsEntity.info.isNullOrEmpty(), this)


        header.layout(R.layout.item_header_tracks)
            .listHeader(context?.getString(R.string.tracks))
            .addTo(this)

        data?.forEachIndexed { index, trackEntity ->
            TrackModel_()
                .id(trackEntity.hashCode())
                .number(index + 1)
                .item(trackEntity)
                .addTo(this)
        }


        emptyState.addIf(data.isNullOrEmpty(), this)

    }

    public override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            (recyclerView.layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
        }
    }
}
