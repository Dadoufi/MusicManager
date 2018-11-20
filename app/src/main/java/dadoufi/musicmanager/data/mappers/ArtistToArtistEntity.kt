package dadoufi.musicmanager.data.mappers

import dadoufi.musicmanager.data.entities.ArtistEntity
import dadoufi.musicmanager.remote.model.Artist
import javax.inject.Inject

class ArtistToArtistEntity @Inject constructor() :
    PagedMapper<Artist, ArtistEntity> {
    override fun map(from: Artist, page: Int) = ArtistEntity(
        icon = from.findLastImage(),
        name = from.name,
        artistQuery = from.artistQuery,
        page = page
    )

}