package dadoufi.musicmanager.data.mappers

import dadoufi.musicmanager.data.entities.TrackEntity
import dadoufi.musicmanager.remote.model.Track
import javax.inject.Inject

class TrackToTrackEntity @Inject constructor() : IndexedParamMapper<Track, TrackEntity, Long> {
    override fun map(from: Track, param: Long?, index: Int): TrackEntity {
        return TrackEntity(
            id = from.hashCode().toLong(),
            name = from.name,
            index = index,
            mid = param,
            duration = from.duration
        )
    }
}