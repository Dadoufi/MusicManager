package dadoufi.musicmanager.data.repositories.topalbum

import androidx.paging.DataSource
import dadoufi.musicmanager.data.DatabaseTransactionRunner
import dadoufi.musicmanager.data.daos.AlbumDao
import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.data.entities.UiState
import dadoufi.musicmanager.data.mappers.AlbumToAlbumEntity
import dadoufi.musicmanager.data.repositories.PagedNetworkRepository
import dadoufi.musicmanager.remote.LastFmService
import dadoufi.musicmanager.remote.model.Album
import dadoufi.musicmanager.ui.album.PAGE_SIZE
import dadoufi.musicmanager.util.AppRxSchedulers
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.internal.operators.completable.CompletableFromAction
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TopAlbumRepository @Inject constructor(
    private val albumDao: AlbumDao,
    private val lastFmService: LastFmService,
    private val schedulers: AppRxSchedulers,
    private val transactionRunner: DatabaseTransactionRunner,
    private val mapper: AlbumToAlbumEntity
) : PagedNetworkRepository<AlbumEntity>(),
    TopAlbumRemoteDataSource,
    TopAlbumLocalDataSource {


    override fun getLastPage(query: String): Maybe<Int> {
        return albumDao.getLastPage(query)
    }


    override fun getPagedDataSource(query: String?): DataSource.Factory<Int, AlbumEntity> {
        return albumDao.getPagedAlbums(query)
    }


    override fun refresh(query: String): Flowable<UiState<Any>> = getTopAlbums(query)

    override fun loadNextPage(query: String): Flowable<UiState<Any>> {
        return getLastPage(query)
            .subscribeOn(schedulers.database)
            .defaultIfEmpty(0)
            .toFlowable()
            .flatMap { lastPage -> getTopAlbums(query, lastPage + 1) }

    }


    override fun getTopAlbums(
        artist: String,
        page: Int,
        pageSize: Int
    ): Flowable<UiState<*>> {
        return lastFmService.getTopAlbums(artist, page)
            .subscribeOn(schedulers.network)
            .map<UiState<*>> { topAlbumResponse ->
                if (topAlbumResponse.error != null && topAlbumResponse.message != null) {
                    UiState.Error.RefreshError(topAlbumResponse.message)
                } else {

                    val albums = topAlbumResponse.topAlbums.album

                    if (albums.isNullOrEmpty()) {
                        if (page == 1) {
                            UiState.Error.RefreshError("No albums found") //todo custom exception and then parse throwable for string
                        } else {
                            UiState.Error.PagedEmptyError()
                        }
                    } else {
                        transactionRunner.run {
                            if (page == 1) {
                                albumDao.deleteAllNonFavoriteForArtist(artist)
                            }
                            albumDao.upsertEntities(
                                convertRemoteToLocal(
                                    albums,
                                    artist,// sometimes the artist name within the object is different from the actual artist query.We use the query
                                    topAlbumResponse.topAlbums.attr.page.toInt()
                                )
                            )
                        }
                        UiState.Success<Any>()
                    }
                }

            }
            .startWith(
                when {
                    page > 1 -> UiState.LoadMore
                    else -> UiState.Loading
                }
            )
            .onErrorResumeNext { throwable: Throwable ->
                val message = throwable.message
                when {
                    page > 1 -> Flowable.just(UiState.Error.PagedError(message))
                    else -> Flowable.just(UiState.Error.RefreshError(message))
                }
            }
            .observeOn(schedulers.main)
    }


    private fun convertRemoteToLocal(
        remoteAlbums: List<Album>,
        artistName: String,
        page: Int
    ): List<AlbumEntity> {
        return remoteAlbums.asSequence().filter { it.name != null }
            .mapIndexedTo(mutableListOf()) { index, album ->
                mapper.map(
                    album,
                    artistName,
                    page,
                    ((page - 1) * PAGE_SIZE + index).toLong()
                )
            }
    }


    override fun saveAlbums(albums: List<AlbumEntity>) {
        albumDao.insertAll(albums)
    }

    override fun updateFavorite(album: AlbumEntity): Completable {
        return CompletableFromAction {
            albumDao.updateAlbum(
                mid = album.mid,
                favorite = !album.favorite
            )
        }
            .subscribeOn(schedulers.database)
            .observeOn(schedulers.main)
    }


}