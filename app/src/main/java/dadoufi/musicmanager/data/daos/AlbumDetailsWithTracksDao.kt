package dadoufi.musicmanager.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dadoufi.musicmanager.data.entities.AlbumDetailsWithTracksEntity
import io.reactivex.Flowable


@Dao
abstract class AlbumDetailsWithTracksDao {

    @Transaction
    @Query("SELECT * FROM album_info WHERE album_name = :albumName")
    abstract fun getAllTracksForAlbum(albumName: String): Flowable<AlbumDetailsWithTracksEntity>


    /*  @Transaction
      @Query("SELECT * FROM album_info INNER JOIN tracks ON album_name == track_album_name WHERE album_name = :albumName ORDER BY `index` ASC")  //order by does not seem to work
      abstract fun getAllTracksForAlbum(albumName: String): Flowable<AlbumDetailsWithTracksEntity>*/

}