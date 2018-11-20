package dadoufi.musicmanager.data.dao

import dadoufi.musicmanager.data.daos.AlbumDao
import dadoufi.musicmanager.data.daos.AlbumDetailsDao
import dadoufi.musicmanager.data.daos.ArtistDao
import dadoufi.musicmanager.data.daos.TrackDao
import dadoufi.musicmanager.util.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test


class DaoDataTest : BaseDatabaseTest() {


    private lateinit var albumDao: AlbumDao
    private lateinit var albumDetailsDao: AlbumDetailsDao
    private lateinit var trackDao: TrackDao
    private lateinit var artistDao: ArtistDao

    @Before
    fun setUp() {
        super.setup()
        albumDao = db.albumDao()
        artistDao = db.artistDao()
        albumDetailsDao = db.albumDetailsDao()
        trackDao = db.trackDao()
    }

    @Test
    fun testRemoveFavorite() {
        albumDao.insert(albumEntity1)
        albumDao.delete(albumEntity1)
        assertThat(albumDao.albumWithAlbumId(album1Id), `is`(nullValue()))

    }


    @Test
    fun testInsertFavoriteSameName() {
        albumDao.insert(favoriteAlbumSameName1)
        albumDao.insert(favoriteAlbumSameName2)

        assertThat(albumDao.getAllFavoriteAlbums(), `is`(listOf(favoriteAlbumSameName1, favoriteAlbumSameName2)))

    }

    @Test
    fun testGetLastPageExists() {
        albumDao.insert(albumEntity1)
        albumDao.getLastPage(albumEntity1.artistName).toSingle(0).test()
            .assertValue(1)
    }


    @Test
    fun testGetLastPageNotExists() {
        albumDao.getLastPage(albumEntity1.artistName).toSingle(0).test()
            .assertValue(0)
    }


    @Test
    fun testInsertAlbumsEmptyTable() {
        albumDao.upsertEntities(topAlbumsEmptyTable)
        albumDao.getAlbumsForArtist(topAlbumEntity1.artistName).test().assertValue(topAlbums)
    }

    @Test
    fun testInsertDeleteArtists() {
        artistDao.insertAll(artists)
        assertThat(artistDao.getArtists("black"), `is`((listOf(artist1, artist2, artist3))))
        artistDao.deleteAllForArtist("black")
        assertThat(artistDao.getArtists("black"), `is`(emptyList()))
    }

    @Test
    fun testAlbumInfoForAlbum() {
        albumDetailsDao.insert(albumDetails1)
        assertThat(albumDetailsDao.getAlbumDetail(albumDetails1.albumName!!), `is`(albumDetails1))

        albumDetailsDao.deleteAlbumDetailsForAlbum(albumDetails1.albumName!!)
        assertThat(albumDetailsDao.getAlbumDetail(albumDetails1.albumName!!), `is`(nullValue()))

    }

}