package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class Streamable(
    @SerializedName("#text")
    val Text: String?,
    @SerializedName("fulltrack")
    val fulltrack: String?
)