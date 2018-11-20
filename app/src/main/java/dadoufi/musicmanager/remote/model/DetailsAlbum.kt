package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class DetailsAlbum(
    @SerializedName("image")
    override val image: List<Image>?,
    @SerializedName("mbid")
    val mbid: String?,
    @SerializedName("listeners")
    val listeners: String?,
    @SerializedName("playcount")
    val playcount: String?,
    @SerializedName("artist")
    val artist: String?,
    @SerializedName("wiki")
    val wiki: Wiki?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("tracks")
    val tracks: Tracks,
    @SerializedName("tags")
    val tags: Tags
) : ImageModel() {
    fun generateId(): Long {
        return (name + artist).hashCode().toLong()
    }
}
