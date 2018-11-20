package dadoufi.musicmanager.data.repositories.artist

import androidx.paging.DataSource
import dadoufi.musicmanager.data.daos.ArtistDao
import dadoufi.musicmanager.data.entities.ArtistEntity
import dadoufi.musicmanager.data.entities.UiState
import dadoufi.musicmanager.data.mappers.ArtistToArtistEntity
import dadoufi.musicmanager.data.repositories.PagedNetworkRepository
import dadoufi.musicmanager.remote.LastFmService
import dadoufi.musicmanager.remote.model.Artist
import dadoufi.musicmanager.util.AppRxSchedulers
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject


class SearchArtistRepository @Inject constructor(
    private val artistDao: ArtistDao,
    private val lastFmService: LastFmService,
    private val schedulers: AppRxSchedulers,
    private val mapper: ArtistToArtistEntity
) : PagedNetworkRepository<ArtistEntity>(),
    SearchArtistRemoteDataSource,
    SearchArtistLocalDataSource {


    override fun getLastPage(query: String): Maybe<Int> {
        return artistDao.getLastPage(query)
    }


    override fun getPagedDataSource(query: String?): DataSource.Factory<Int, ArtistEntity> {
        return artistDao.getPagedArtists(query!!)
    }


    override fun refresh(query: String): Flowable<UiState<*>> = searchArtist(query)

    override fun loadNextPage(query: String): Flowable<UiState<*>> {
        return getLastPage(query)
            .subscribeOn(schedulers.database)
            .toSingle(0)
            .toFlowable()
            .flatMap { lastPage -> searchArtist(query, lastPage + 1) }

    }


    override fun deleteAll(): Completable =
        Completable.fromAction {
            artistDao.deleteAll()
        }
            .subscribeOn(schedulers.database)
            .observeOn(schedulers.main)

    override fun delete(artists: List<ArtistEntity>): Completable {
        return artistDao.deleteCompletable(artists)
            .subscribeOn(schedulers.database)
            .observeOn(schedulers.main)
    }

    override fun searchArtist(
        artistQuery: String,
        page: Int,
        pageSize: Int
    ): Flowable<UiState<*>> {
        return lastFmService.searchArtist(artistQuery, page)
            .subscribeOn(schedulers.network)
            .map<UiState<Any>> { searchArtistResponse ->
                val searchTerms = searchArtistResponse.results.openSearchQuery.searchTerms
                val artists = searchArtistResponse.results.artistMatches.artist

                if (page == 1) {
                    artistDao.deleteAllForArtist(searchTerms)
                }

                if (artists.isNullOrEmpty()) {
                    if (page == 1) {
                        UiState.Error.RefreshError("No artists found") //todo custom exception and then parse throwable for string
                    } else {
                        UiState.Error.PagedEmptyError()
                    }

                } else {
                    artists.let {
                        artistDao.insertAll(
                            convertRemoteToLocal(
                                it,
                                searchArtistResponse.results.openSearchQuery.startPage.toInt(),
                                searchTerms
                            )
                        )
                    }
                    UiState.Success()
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
        remoteArtists: List<Artist>,
        page: Int,
        searchQuery: String
    ): List<ArtistEntity> {
        return remoteArtists.mapTo(mutableListOf()) {
            it.artistQuery = searchQuery
            mapper.map(it, page)
        }
    }


}




