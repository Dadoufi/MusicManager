package dadoufi.musicmanager.data.mappers

import dadoufi.musicmanager.data.entities.AlbumDetailsEntity
import dadoufi.musicmanager.remote.model.AlbumDetailsResponse
import javax.inject.Inject

class AlbumDetailsToAlbumDetailsEntity @Inject constructor() :
    Mapper<AlbumDetailsResponse, AlbumDetailsEntity> {
    override fun map(from: AlbumDetailsResponse): AlbumDetailsEntity {
        return AlbumDetailsEntity(
            id = from.hashCode().toLong(),
            mid = from.album.generateId(),
            albumName = from.album.name,
            albumIcon = from.album.findLastImage(),
            artistName = from.album.artist,
            _tags = from.album.tags.getTagAsList(),
            info = from.album.wiki?.run { getNonUrlWiki(this.summary) },
            published = from.album.wiki?.published,
            playCount = from.album.playcount,
            listeners = from.album.listeners
        )


    }


}