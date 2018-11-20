package dadoufi.musicmanager.data.mappers

import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.remote.model.Album
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AlbumToAlbumEntity @Inject constructor() :
    IndexedPagedParamMapper<Album, AlbumEntity, String?> {
    override fun map(from: Album, param: String?, page: Int, index: Long) = AlbumEntity(
        mid = from.generateId(),
        albumIcon = from.findLastImage(),
        albumName = from.name,
        artistName = param,
        page = page,
        index = index,
        favorite = false
    )

}