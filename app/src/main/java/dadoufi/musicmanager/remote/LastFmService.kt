package dadoufi.musicmanager.remote

import dadoufi.musicmanager.remote.model.AlbumDetailsResponse
import dadoufi.musicmanager.remote.model.SearchArtistResponse
import dadoufi.musicmanager.remote.model.TopAlbumResponse
import dadoufi.musicmanager.ui.album.PAGE_SIZE
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * REST API access points
 */
interface LastFmService {

    @GET("?method=artist.search")
    fun searchArtist(@Query("artist") artistQuery: String? = "", @Query("page") page: Int = 1): Flowable<SearchArtistResponse>

    @GET("?method=artist.gettopalbums")
    fun getTopAlbums(
        @Query("artist") artist: String? = "", @Query("page") page: Int = 1, @Query("limit") limit: Int = PAGE_SIZE, @Query(
            "autocorrect"
        ) autocorrect: Int = 1
    ): Flowable<TopAlbumResponse>


    @GET("?method=album.getinfo")
    fun getAlbumInfo(
        @Query("artist") artist: String? = "", @Query("album") album: String? = "", @Query(
            "autocorrect"
        ) autocorrect: Int = 1
    ): Flowable<AlbumDetailsResponse>


}
