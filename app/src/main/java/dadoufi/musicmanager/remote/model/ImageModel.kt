package dadoufi.musicmanager.remote.model

abstract class ImageModel {
    @Transient
    open val image: List<Image>? = null


    fun findLastImage(): String? {
        return image?.findLast { !it.text.isNullOrBlank() }?.text
    }
}