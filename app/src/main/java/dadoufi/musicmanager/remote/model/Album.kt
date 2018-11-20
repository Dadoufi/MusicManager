package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("image")
    override val image: List<Image>?,
    @SerializedName("mbid")
    val mbid: String?,
    @SerializedName("artist")
    val artist: Artist?,
    @SerializedName("name")
    val name: String?
) : ImageModel() {

    fun generateId(): Long {
        return (name + artist?.name).hashCode().toLong()
    }
}