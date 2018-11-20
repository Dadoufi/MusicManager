package dadoufi.musicmanager.data.entities

import androidx.room.*
import androidx.room.Entity
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "tracks",

    indices = [
        Index(value = ["name"], unique = true),
        Index(value = ["mid"], unique = false),
        Index(value = ["id"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = AlbumEntity::class,
            parentColumns = arrayOf("mid"),
            childColumns = arrayOf("mid"),
            onDelete = CASCADE
        )
    ]

)
data class TrackEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "index") val index: Int? = 0,
    @ColumnInfo(name = "mid") val mid: Long? = 0,
    @ColumnInfo(name = "duration") val duration: String?
) {


    /* override fun equals(other: Any?): Boolean {
         if (this === other) return true
         if (other !is TrackEntity) return false

         if (name != other.name) return false
         if (albumName != other.albumName) return false
         if (duration != other.duration) return false

         return true
     }

     override fun hashCode(): Int {
         var result = name?.hashCode() ?: 0
         result = 31 * result + (albumName?.hashCode() ?: 0)
         result = 31 * result + (duration?.hashCode() ?: 0)
         return result
     }*/
}