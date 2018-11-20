package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class TopAlbumResponse(
    @SerializedName("topalbums")
    val topAlbums: TopAlbums,
    @SerializedName("error")
    override val error: Int?,
    @SerializedName("message")
    override val message: String?
) : ApiError
