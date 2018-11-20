package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("duration")
    val duration: String?,
    @SerializedName("@attr")
    val Attr: Attr,
    @SerializedName("streamable")
    val streamable: Streamable,
    @SerializedName("artist")
    val artist: Artist?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)