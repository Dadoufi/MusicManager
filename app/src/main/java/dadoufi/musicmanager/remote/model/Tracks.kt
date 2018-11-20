package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class Tracks(
    @SerializedName("track")
    val track: List<Track>? = null
)