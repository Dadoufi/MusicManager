package dadoufi.musicmanager.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "artists",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["name"], unique = true)
    ]
)
@Parcelize
data class ArtistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "artist_icon") val icon: String? = null,
    @ColumnInfo(name = "artist_query") val artistQuery: String? = null,
    @ColumnInfo(name = "page") override val page: Int? = null
) : PagedEntity, Parcelable {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ArtistEntity) return false

        if (name != other.name) return false
        if (icon != other.icon) return false
        if (artistQuery != other.artistQuery) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + (artistQuery?.hashCode() ?: 0)
        return result
    }


}