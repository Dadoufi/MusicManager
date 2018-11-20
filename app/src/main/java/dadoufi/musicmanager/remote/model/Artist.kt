package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName
import dadoufi.musicmanager.remote.util.Exclude

data class Artist(
    @SerializedName("image")
    override val image: List<Image>?,
    @SerializedName("mbid")
    val mbid: String = "",
    @SerializedName("listeners")
    val listeners: String = "",
    @SerializedName("streamable")
    val streamable: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("url")
    val url: String = "",
    @Exclude
    var artistQuery: String
) : ImageModel()