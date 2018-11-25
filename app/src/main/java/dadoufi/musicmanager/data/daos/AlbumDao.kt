package dadoufi.musicmanager.data.daos

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dadoufi.musicmanager.data.entities.AlbumEntity
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
abstract class AlbumDao : EntityDao<AlbumEntity>, PaginatedDao {

    @Transaction
    @Query("SELECT * FROM albums WHERE artist_name = :artistName  ORDER BY page ASC, `index` ASC ")
    abstract fun getPagedAlbums(artistName: String?): DataSource.Factory<Int, AlbumEntity>

    @Transaction
    @Query("SELECT * FROM albums WHERE artist_name = :artistName  ORDER BY page ASC, id ASC ")
    abstract fun getAlbumsForArtist(artistName: String?): Flowable<List<AlbumEntity>>

    @Transaction
    @Query("SELECT * FROM albums WHERE favorite = 1 ORDER BY artist_name ASC")
    abstract fun getFavoriteAlbums(): DataSource.Factory<Int, AlbumEntity>

    @Query("DELETE FROM albums")
    abstract fun deleteAll()

    @Query("DELETE FROM albums WHERE artist_name = :artist ")
    abstract fun deleteAllForArtist(artist: String)

    @Query("DELETE FROM albums WHERE artist_name = :artist AND favorite = 0")
    abstract fun deleteAllNonFavoriteForArtist(artist: String)

    @Query("SELECT * from albums WHERE mid = :mid")
    abstract fun albumWithAlbumId(mid: Long): AlbumEntity?

    @Query("SELECT * from albums WHERE favorite = 1")
    abstract fun getAllFavoriteAlbums(): List<AlbumEntity>

    @Query("UPDATE albums SET `index`= :index, album_name = :albumName, album_icon = :albumIcon, artist_name = :artistName, page = :page WHERE mid = :mid")
    abstract fun updateAlbum(
        mid: Long,
        albumName: String?,
        albumIcon: String?,
        artistName: String?,
        page: Int,
        index: Long
    )

    @Query("UPDATE albums SET favorite = :favorite WHERE mid = :mid")
    abstract fun updateAlbum(
        mid: Long,
        favorite: Boolean
    )

    @Query("SELECT favorite FROM albums WHERE mid = :mid")
    abstract fun getAlbum(
        mid: Long
    ): Boolean

    @Query("SELECT MAX(page) FROM albums WHERE artist_name = :query")
    abstract override fun getLastPage(query: String?): Maybe<Int>


    @Transaction
    open fun insertAndDeleteInTransaction(albums: List<AlbumEntity>) {
        deleteAll()
        insertAll(albums)
    }

    @Transaction
    open fun upsertEntities(albums: List<AlbumEntity>) {
        albums.map {
            val id = insertIgnore(it)
            if (id == -1L) {
                updateAlbum(
                    it.mid,
                    it.albumName,
                    it.albumIcon,
                    it.artistName,
                    it.page,
                    it.index
                )
            }
        }
    }
}

