package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class Wiki(
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("published")
    val published: String?,
    @SerializedName("content")
    val content: String?
) {

    fun getNonUrlWiki(wiki: String?): String? {
        val replacement = wiki?.replace(regex = Regex(pattern = regex), replacement = "")
        return replacement
    }

    companion object {
        const val regex = "</?a.*[^>]*>"
    }

}