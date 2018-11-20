package dadoufi.musicmanager.remote.model

import com.google.gson.annotations.SerializedName

data class Tags(
    @SerializedName("tag")
    val tag: List<Tag>?
) {

    fun getTagAsList(): String {
        val tags = emptyList<String>().toMutableList()
        tag?.map { tag ->
            tag.name?.let(tags::add)
        }

        return tags.joinToString(",")
    }

}