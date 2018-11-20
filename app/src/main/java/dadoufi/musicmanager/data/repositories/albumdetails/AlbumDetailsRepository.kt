package dadoufi.musicmanager.data.repositories.albumdetails

import dadoufi.musicmanager.data.daos.AlbumDetailsDao
import dadoufi.musicmanager.data.daos.AlbumDetailsWithTracksDao
import dadoufi.musicmanager.data.daos.TrackDao
import dadoufi.musicmanager.data.entities.AlbumDetailsWithTracksEntity
import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.data.entities.UiState
import dadoufi.musicmanager.data.mappers.AlbumDetailsToAlbumDetailsEntity
import dadoufi.musicmanager.data.mappers.TrackToTrackEntity
import dadoufi.musicmanager.data.repositories.Repository
import dadoufi.musicmanager.remote.LastFmService
import dadoufi.musicmanager.remote.model.AlbumDetailsResponse
import dadoufi.musicmanager.util.AppRxSchedulers
import io.reactivex.Flowable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumDetailsRepository @Inject constructor(
    private val albumDetailsDao: AlbumDetailsDao,
    private val trackDao: TrackDao,
    private val albumDetailsWithTracksDao: AlbumDetailsWithTracksDao,
    private val lastFmService: LastFmService,
    private val schedulers: AppRxSchedulers,
    private val albumDetailsToAlbumDetailsEntity: AlbumDetailsToAlbumDetailsEntity,
    private val trackMapper: TrackToTrackEntity
) : Repository<AlbumEntity, AlbumDetailsWithTracksEntity>,
    AlbumDetailsRemoteDataSource,
    AlbumDetailsLocalDataSource {


    override fun refresh(
        query: AlbumEntity,
        forceRefresh: Boolean
    ): Flowable<UiState<AlbumDetailsWithTracksEntity>> =
        getAlbumInfo(
            query.albumName?.let { getLocalAlbumInfo(it) },
            if (forceRefresh) getRemoteAlbumInfo(query.artistName, query.albumName) else null
        )


    override fun getLocalAlbumInfo(albumName: String): Flowable<AlbumDetailsWithTracksEntity> =
        albumDetailsWithTracksDao.getAllTracksForAlbum(albumName)
            .subscribeOn(schedulers.database)

    override fun getRemoteAlbumInfo(
        artist: String?,
        album: String?
    ): Flowable<AlbumDetailsWithTracksEntity> = lastFmService.getAlbumInfo(artist, album)
        .subscribeOn(schedulers.network)
        .doOnNext { response: AlbumDetailsResponse ->
            album?.let { album ->
                albumDetailsDao.deleteAlbumDetailsForAlbum(albumName = album)

                response.album.name?.let(albumDetailsDao::deleteAlbumDetailsForAlbum) //todo empty tracks handling
                albumDetailsDao.insertReplace(albumDetailsToAlbumDetailsEntity.map(response))

                val list = response.album.tracks.track?.mapIndexedTo(mutableListOf()) { index, it ->
                    trackMapper.map(it, response.album.generateId(), index)
                }
                list?.toList()?.let { trackDao.insertAll(it) }
                Timber.d("inserted")
            }
        }
        .concatMap {
            album?.let { album ->
                Timber.d("Returned")
                getLocalAlbumInfo(album)
            }
        }


    private fun getAlbumInfo(
        localSource: Flowable<AlbumDetailsWithTracksEntity>? = null,
        remoteSource: Flowable<AlbumDetailsWithTracksEntity>? = null
    ): Flowable<UiState<AlbumDetailsWithTracksEntity>> {
        return Flowable.concatEager(
            listOf(
                localSource,
                remoteSource?.delay(50, TimeUnit.MILLISECONDS, schedulers.main)
            ).filter { it != null }
        )
            .distinct()
            .map<UiState<AlbumDetailsWithTracksEntity>> {
                UiState.Success(it)
            }
            .startWith(UiState.Loading)
            .onErrorReturn {
                UiState.Error.RefreshError(it.message)
            }
            .observeOn(schedulers.main)
    }
}



