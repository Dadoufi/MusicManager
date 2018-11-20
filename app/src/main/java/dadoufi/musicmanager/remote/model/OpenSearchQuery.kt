package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class OpenSearchQuery(
    @SerializedName("startPage")
    val startPage: String = "",
    @SerializedName("#text")
    val Text: String = "",
    @SerializedName("role")
    val role: String = "",
    @SerializedName("searchTerms")
    val searchTerms: String = ""
)