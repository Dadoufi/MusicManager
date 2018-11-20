package dadoufi.musicmanager.data.entities


interface Entity

interface PagedEntity : Entity {
    val page: Int?
}