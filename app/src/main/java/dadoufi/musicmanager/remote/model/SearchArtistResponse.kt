package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class SearchArtistResponse(
    @SerializedName("results")
    val results: Results
)