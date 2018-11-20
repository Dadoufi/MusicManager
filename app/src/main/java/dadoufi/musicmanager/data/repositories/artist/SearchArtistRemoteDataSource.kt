package dadoufi.musicmanager.data.repositories.artist

import dadoufi.musicmanager.data.entities.UiState
import io.reactivex.Flowable

interface SearchArtistRemoteDataSource {

    fun searchArtist(
        artistQuery: String,
        page: Int = 1,
        pageSize: Int = 50
    ): Flowable<UiState<Any>>


}