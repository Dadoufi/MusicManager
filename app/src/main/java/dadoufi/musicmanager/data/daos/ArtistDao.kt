package dadoufi.musicmanager.data.daos

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dadoufi.musicmanager.data.entities.ArtistEntity
import io.reactivex.Maybe


@Dao
abstract class ArtistDao : EntityDao<ArtistEntity>, PaginatedDao {

    @Transaction
    @Query("SELECT * FROM artists  WHERE artist_query =:artist ORDER BY  page ASC")
    abstract fun getPagedArtists(artist: String): DataSource.Factory<Int, ArtistEntity>


    @Query("DELETE FROM artists")
    abstract fun deleteAll()

    @Query("DELETE FROM artists WHERE artist_query =:artist ")
    abstract fun deleteAllForArtist(artist: String)


    @Query("SELECT MAX(page) FROM artists WHERE artist_query =:query ")
    abstract override fun getLastPage(query: String?): Maybe<Int>


    @Query("SELECT * FROM artists WHERE artist_query = :artist  ")
    abstract fun getArtists(artist: String?): List<ArtistEntity>

    @Transaction
    open fun insertAndDeleteInTransaction(albums: List<ArtistEntity>) {
        deleteAll()
        insertAll(albums)
    }

}
