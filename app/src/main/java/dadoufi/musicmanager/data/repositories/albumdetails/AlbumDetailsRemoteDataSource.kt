package dadoufi.musicmanager.data.repositories.albumdetails

import dadoufi.musicmanager.data.entities.AlbumDetailsWithTracksEntity
import io.reactivex.Flowable

interface AlbumDetailsRemoteDataSource {

    fun getRemoteAlbumInfo(artist: String?, album: String?): Flowable<AlbumDetailsWithTracksEntity>

}