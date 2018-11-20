package dadoufi.musicmanager.data.repositories.favorite

import android.annotation.SuppressLint
import androidx.paging.DataSource
import dadoufi.musicmanager.data.daos.AlbumDao
import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.data.repositories.PagedRepository
import dadoufi.musicmanager.util.AppRxSchedulers
import io.reactivex.Completable
import javax.inject.Inject

@SuppressLint("CheckResult")
class FavoriteRepository @Inject constructor(
    private val albumDao: AlbumDao,
    private val schedulers: AppRxSchedulers
) : PagedRepository<AlbumEntity>,
    FavoriteDataSource {


    override fun getPagedDataSource(query: String?): DataSource.Factory<Int, AlbumEntity> {
        return albumDao.getFavoriteAlbums()
    }


    override fun removeFavorite(mid: Long): Completable =
        Completable.fromAction { albumDao.updateAlbum(mid, false) }
            .subscribeOn(schedulers.database)
            .observeOn(schedulers.main)

}