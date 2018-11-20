package dadoufi.musicmanager.data.daos

import androidx.room.Dao
import androidx.room.Query
import dadoufi.musicmanager.data.entities.AlbumDetailsEntity

@Dao
abstract class AlbumDetailsDao : EntityDao<AlbumDetailsEntity> {



    @Query("DELETE  FROM album_info WHERE album_name = :albumName")
    abstract fun deleteAlbumDetailsForAlbum(albumName: String)


    @Query("SELECT *  FROM album_info WHERE album_name = :albumName")
    abstract fun getAlbumDetail(albumName: String): AlbumDetailsEntity?

}