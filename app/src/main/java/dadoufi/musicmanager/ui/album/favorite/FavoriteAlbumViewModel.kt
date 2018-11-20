package dadoufi.musicmanager.ui.album.favorite

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import dadoufi.musicmanager.base.PagedViewModel
import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.data.repositories.favorite.FavoriteRepository
import dadoufi.musicmanager.util.AppRxSchedulers
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

@SuppressLint("RxSubscribeOnError")
class FavoriteAlbumViewModel @Inject constructor(
    schedulers: AppRxSchedulers,
    override var repository: FavoriteRepository
) : PagedViewModel<Any, AlbumEntity>(schedulers, repository) {


    var observer: Observer<PagedList<AlbumEntity>?>? = null


    fun removeFavoriteAlbum(mid: Long) {
        disposables += repository.removeFavorite(mid)
            .subscribe()
    }

    override fun onCleared() {
        super.onCleared()
        if (liveList.hasObservers()) {
            observer?.let { liveList.removeObserver(it) }
        }
    }

}