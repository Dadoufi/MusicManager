package dadoufi.musicmanager.data.entities

import androidx.room.Embedded
import androidx.room.Relation
import dadoufi.musicmanager.extensions.asDuration
import java.util.*
import java.util.concurrent.TimeUnit


class AlbumDetailsWithTracksEntity : Entity {


    @Embedded
    var albumDetails: AlbumDetailsEntity? = null
    @Relation(parentColumn = "mid", entityColumn = "mid")
    var relations: List<TrackEntity>? = null


    fun getRelationsSorted(): List<TrackEntity>? = relations?.sortedBy { it.index }


    fun getTotalDuration(): String {
        var total = 0
        relations?.map {
            total += it.duration?.toIntOrNull() ?: 0
        }
        return total.asDuration(TimeUnit.HOURS)
    }

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is AlbumDetailsWithTracksEntity -> albumDetails == other.albumDetails && relations == other.relations
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(albumDetails, relations)


}
