package dadoufi.musicmanager.data.entities

import androidx.room.*
import androidx.room.Entity

@Entity(
    tableName = "album_info",
    indices = [
        Index(value = ["album_name"], unique = true),
        Index(value = ["mid"], unique = true),
        Index(value = ["id"], unique = true),
        Index(value = ["artist_name"])
    ]
)
data class AlbumDetailsEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "mid") val mid: Long = 0,
    @ColumnInfo(name = "album_name") val albumName: String?,
    @ColumnInfo(name = "album_icon") val albumIcon: String?,
    @ColumnInfo(name = "artist_name") val artistName: String?,
    @ColumnInfo(name = "tags") val _tags: String?,
    @ColumnInfo(name = "info") val info: String?,
    @ColumnInfo(name = "published") val published: String?,
    @ColumnInfo(name = "listeners") val listeners: String?,
    @ColumnInfo(name = "play_count") val playCount: String?
) {

    @delegate:Ignore
    val tags by lazy(LazyThreadSafetyMode.NONE) {
        _tags?.split(",") ?: emptyList()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AlbumDetailsEntity) return false

        if (albumName != other.albumName) return false
        if (albumIcon != other.albumIcon) return false
        if (artistName != other.artistName) return false
        if (_tags != other._tags) return false
        if (info != other.info) return false
        if (published != other.published) return false
        if (listeners != other.listeners) return false
        if (playCount != other.playCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = albumName?.hashCode() ?: 0
        result = 31 * result + (albumIcon?.hashCode() ?: 0)
        result = 31 * result + (artistName?.hashCode() ?: 0)
        result = 31 * result + (_tags?.hashCode() ?: 0)
        result = 31 * result + (info?.hashCode() ?: 0)
        result = 31 * result + (published?.hashCode() ?: 0)
        result = 31 * result + (listeners?.hashCode() ?: 0)
        result = 31 * result + (playCount?.hashCode() ?: 0)
        return result
    }


}


//_genres = from.genres?.joinToString(",")