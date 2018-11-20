package dadoufi.musicmanager.util

import dadoufi.musicmanager.data.entities.AlbumDetailsEntity
import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.data.entities.ArtistEntity
import dadoufi.musicmanager.remote.model.*

const val album1Id = 1L
val albumEntity1 = AlbumEntity(
    id = album1Id,
    mid = album1Id,
    albumName = "Enter Sandman",
    albumIcon = "cool icon",
    artistName = "Metallica",
    favorite = true,
    page = 1,
    index = album1Id
)

const val album2Id = 2L
val albumEntity2 = AlbumEntity(
    id = album2Id,
    mid = album2Id,
    albumName = "Black Holes and Revelations",
    albumIcon = "Knights of Cydonia",
    artistName = "Muse",
    favorite = false,
    page = 1,
    index = album2Id
)

const val album3Id = 3L
val albumEntity3 = AlbumEntity(
    id = album3Id,
    mid = album3Id,
    albumName = "Otherside",
    albumIcon = "Knights of Cydonia",
    artistName = "Red Hot Chili Peppers",
    favorite = false,
    page = 1,
    index = album3Id
)

const val album4Id = 4L
val topAlbumEntity1 = AlbumEntity(
    id = album4Id,
    mid = album4Id,
    albumName = "Highway to Hell",
    albumIcon = "Highway to Hell",
    artistName = "AC/DC",
    favorite = false,
    page = 1,
    index = 4
)


const val album5Id = 5L
val topAlbumEntity2 = AlbumEntity(
    id = album5Id,
    mid = album5Id,
    albumName = "ThunderStruck",
    albumIcon = "ThunderStruck",
    artistName = "AC/DC",
    favorite = false,
    page = 1,
    index = album5Id
)

val remoteAlbum1 = Album(
    null, "45345",
    Artist(
        null,
        "123123",
        "123", "",
        "Cher", "",
        ""
    ), "Greatest Hits"
)

val remoteAlbum2 = Album(
    null, "3456",
    Artist(
        null,
        "56757",
        "123", "",
        "Red hot Chili Peppers", "",
        ""
    ), "Greatest Hits"
)

val favoriteAlbumSameName1 = AlbumEntity(
    id = 14,
    mid = remoteAlbum1.generateId(),
    albumName = remoteAlbum1.name,
    albumIcon = "",
    artistName = remoteAlbum1.artist?.name,
    favorite = true,
    page = 1,
    index = 1
)

val favoriteAlbumSameName2 = AlbumEntity(
    id = 15,
    mid = remoteAlbum2.generateId(),
    albumName = remoteAlbum2.name,
    albumIcon = "",
    artistName = remoteAlbum2.artist?.name,
    favorite = true,
    page = 1,
    index = 2
)


val topAlbums = listOf(topAlbumEntity1, topAlbumEntity2)
val topAlbumsEmptyTable = topAlbums

val artist1 = ArtistEntity(
    id = 1,
    name = "The Black Keys",
    artistQuery = "black",
    icon = "cool icon",
    page = 1
)
val artist2 = ArtistEntity(
    id = 2,
    name = "Black Sabath",
    artistQuery = "black",
    icon = "cool icon",
    page = 1
)
val artist3 = ArtistEntity(
    id = 3,
    name = "Black Eyed Peas",
    artistQuery = "black",
    icon = "cool icon",
    page = 1
)

val artist4 = ArtistEntity(
    id = 4,
    name = "Metallica",
    artistQuery = "metallica",
    icon = "cool icon",
    page = 1
)

val artists = listOf(artist1, artist2, artist3, artist4)


val albumDetails1 = AlbumDetailsEntity(
    id = 1,
    mid = 123123,
    albumName = "Highway to Hell",
    artistName = "AC/DC",
    albumIcon = "icon",
    _tags = "tag1,tag2",
    info = "info",
    published = "publised date",
    listeners = "345",
    playCount = "3456"
)


val attr = Attr("cher", "1", "50", "100", "100", "4", "cher")
val topAlbumResponse = TopAlbumResponse(
    topAlbums = TopAlbums(
        listOf(remoteAlbum1),
        attr
    ), error = null, message = null
)






