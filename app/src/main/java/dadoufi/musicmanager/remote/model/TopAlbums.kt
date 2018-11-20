package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class TopAlbums(
    @SerializedName("album") val album: List<Album>,
    @SerializedName("@attr") val attr: Attr
)