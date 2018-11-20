package dadoufi.musicmanager.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.NOCASE
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "albums",
    indices = [
        Index(value = ["album_name"], unique = false),
        Index(value = ["id"], unique = true),
        Index(value = ["mid"], unique = true),
        Index(value = ["artist_name"]),
        Index(value = ["favorite"])
    ]
)
@Parcelize
data class AlbumEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "mid") val mid: Long = 0,
    @ColumnInfo(name = "album_name") val albumName: String?,
    @ColumnInfo(name = "album_icon") val albumIcon: String?,
    @ColumnInfo(name = "artist_name", collate = NOCASE) val artistName: String?,
    @ColumnInfo(name = "favorite") var favorite: Boolean,
    @ColumnInfo(name = "page") override val page: Int,
    @ColumnInfo(name = "index") val index: Long
) : PagedEntity, Parcelable