package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class Attr(
    @SerializedName("artist") val artist: String,
    @SerializedName("page") val page: String,
    @SerializedName("perPage") val perPage: String,
    @SerializedName("totalPages") val totalPages: String,
    @SerializedName("total") val total: String,
    @SerializedName("rank") val rank: String,
    @SerializedName("for") val forArtist: String
)