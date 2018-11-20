package dadoufi.musicmanager.data.repositories.artist

import dadoufi.musicmanager.data.entities.ArtistEntity
import io.reactivex.Completable

interface SearchArtistLocalDataSource {


    fun deleteAll(): Completable


    fun delete(artists: List<ArtistEntity>): Completable
}