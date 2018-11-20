package dadoufi.musicmanager.data.repositories.albumdetails

import dadoufi.musicmanager.data.entities.AlbumDetailsWithTracksEntity
import io.reactivex.Flowable

interface AlbumDetailsLocalDataSource {


    fun getLocalAlbumInfo(albumName: String): Flowable<AlbumDetailsWithTracksEntity>
}