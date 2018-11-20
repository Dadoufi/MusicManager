package dadoufi.musicmanager.ui.album.top

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import dadoufi.musicmanager.base.PagedViewModel
import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.data.repositories.PagedRepository.Page
import dadoufi.musicmanager.data.repositories.PagedRepository.Params
import dadoufi.musicmanager.data.repositories.topalbum.TopAlbumRepository
import dadoufi.musicmanager.extensions.distinctUntilChangedNonEmptyList
import dadoufi.musicmanager.ui.album.PAGE_SIZE
import dadoufi.musicmanager.util.AbsentLiveData
import dadoufi.musicmanager.util.AppRxSchedulers
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

@SuppressLint("RxSubscribeOnError")
class TopAlbumViewModel
@Inject constructor(
    schedulers: AppRxSchedulers,
    override val repository: TopAlbumRepository
) : PagedViewModel<Any, AlbumEntity>(schedulers, repository, PAGE_SIZE, 2) {


    val artistName = MutableLiveData<String>()


    override val liveList: LiveData<PagedList<AlbumEntity>> by lazy(mode = LazyThreadSafetyMode.NONE) {
        Transformations.switchMap(artistName) { name ->
            if (name.isNullOrEmpty()) {
                AbsentLiveData.create()
            } else {
                createLivePagedListBuilder(name)
                    .build().distinctUntilChangedNonEmptyList()
            }
        }

    }

    override fun callLoadMore() {
        artistName.value?.let { name ->
            disposables += repository.execute(
                Params(
                    name,
                    Page.LOAD_NEXT_PAGE
                )
            ).subscribe { uiStateLiveData.postValue(it) }
        }


    }

    override fun callRefresh(forceUpdate: Boolean) {
        artistName.value?.let { name ->
            disposables += repository.execute(
                Params(
                    name,
                    Page.REFRESH
                )
            )
                .subscribe {
                    uiStateLiveData.postValue(it)
                }
        }
    }

    fun updateItem(item: AlbumEntity) {
        disposables += repository
            .updateFavorite(item)
            .subscribe()
    }


    fun setId(artist: String?) {
        if (artist.isNullOrEmpty() || artistName.value == artist) {
            return
        }
        artistName.value = artist
        callRefresh()

    }


}






