package dadoufi.musicmanager.remote.model

interface ApiError {
    // @SerializedName("error")
    val error: Int?
    //@SerializedName("message")
    val message: String?

}