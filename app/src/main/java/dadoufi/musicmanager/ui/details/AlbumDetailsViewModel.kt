package dadoufi.musicmanager.ui.details

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import dadoufi.musicmanager.base.BaseViewModel
import dadoufi.musicmanager.data.entities.AlbumDetailsWithTracksEntity
import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.data.repositories.Repository.Params
import dadoufi.musicmanager.data.repositories.Repository.UpdateSource
import dadoufi.musicmanager.data.repositories.albumdetails.AlbumDetailsRepository
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

@SuppressLint("RxSubscribeOnError")
class AlbumDetailsViewModel @Inject constructor(
    private val repository: AlbumDetailsRepository
) : BaseViewModel<AlbumDetailsWithTracksEntity>() {

    private val albumMutableLiveData = MutableLiveData<AlbumEntity>()


    fun setAlbumInfoEntries(album: AlbumEntity) {
        if (album == albumMutableLiveData.value) {
            callRefresh(false)
        } else {
            albumMutableLiveData.value = album
            callRefresh()
        }
    }


    override fun callRefresh(forceUpdate: Boolean) {
        albumMutableLiveData.value?.let { _ ->
            disposables += repository.execute(
                Params(
                    albumMutableLiveData.value!!,
                    if (forceUpdate) UpdateSource.NETWORK else UpdateSource.CACHE
                )
            )
                .subscribe { uiStateLiveData.postValue(it) }
        }

    }

}