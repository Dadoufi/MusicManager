package dadoufi.musicmanager.data.repositories.topalbum

import dadoufi.musicmanager.data.entities.AlbumEntity
import io.reactivex.Completable


interface TopAlbumLocalDataSource {

    fun saveAlbums(albums: List<AlbumEntity>)

    fun updateFavorite(album: AlbumEntity): Completable

}

