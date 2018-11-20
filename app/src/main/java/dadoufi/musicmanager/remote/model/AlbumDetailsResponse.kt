package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class AlbumDetailsResponse(
    @SerializedName("album")
    val album: DetailsAlbum,
    @SerializedName("error")
    override val error: Int,
    @SerializedName("message")
    override val message: String
) : ApiError
