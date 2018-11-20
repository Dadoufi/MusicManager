package dadoufi.musicmanager.data.repositories.topalbum

import dadoufi.musicmanager.data.entities.UiState
import io.reactivex.Flowable

interface TopAlbumRemoteDataSource {

    fun getTopAlbums(
        artist: String,
        page: Int = 1,
        pageSize: Int = 50
    ): Flowable<UiState<*>>
}